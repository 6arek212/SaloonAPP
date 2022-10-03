package com.example.ibrasaloonapp.presentation.ui.login

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.domain.use_case.ValidatePhoneNumber
import com.example.ibrasaloonapp.domain.use_case.ValidateRequired
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.use.LoginUseCase
import com.example.ibrasaloonapp.use.SendAuthVerificationUseCase
import com.example.trainingapp.network.NetworkErrors.ERROR_400
import com.example.trainingapp.network.NetworkErrors.ERROR_403
import com.example.trainingapp.network.NetworkErrors.ERROR_404
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val context: Application,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val required: ValidateRequired,
    private val loginUseCase: LoginUseCase,
    private val sendAuthVerificationUseCase: SendAuthVerificationUseCase
) : BaseViewModel() {

    private val _state: MutableState<LoginState> = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    private var verifyId: String? = null

    fun onTriggerEvent(event: LoginEvent) {
        viewModelScope.launch {
            when (event) {
                is LoginEvent.OnPhoneValueChange -> {
                    _state.value = _state.value.copy(phone = event.value)
                }

                is LoginEvent.OnCodeDigitChanged -> {
                    removeMessage()
                    when (event.codePlace) {
                        CodeDigitPlace.ONE -> {
                            val code = _state.value.verifyCode.copy(one = event.value)
                            _state.value = _state.value.copy(verifyCode = code)
                        }
                        CodeDigitPlace.TWO -> {
                            val code = _state.value.verifyCode.copy(two = event.value)
                            _state.value = _state.value.copy(verifyCode = code)
                        }
                        CodeDigitPlace.THREE -> {
                            val code = _state.value.verifyCode.copy(three = event.value)
                            _state.value = _state.value.copy(verifyCode = code)
                        }
                        CodeDigitPlace.FOUR -> {
                            val code = _state.value.verifyCode.copy(four = event.value)
                            _state.value = _state.value.copy(verifyCode = code)
                            login()
                        }
                    }
                }

                is LoginEvent.Reset -> {
                    rest()
                }

                is LoginEvent.SendAuthVerification -> {
                    sendAuthVerification(event.sendAgain)
                }

                is LoginEvent.OnRemoveHeadFromQueue -> {
                    removeMessage()
                }


                is LoginEvent.Login -> {
                    login()
                }
            }
        }
    }


    private suspend fun sendAuthVerification(sendAgain: Boolean) {
        val phone = _state.value.phone
        val phoneValidate = validatePhoneNumber.execute(phone)

        val hasError = listOf(
            phoneValidate
        ).any { !it.successful }

        _state.value = _state.value.copy(
            phoneError = phoneValidate.errorMessage
        )

        if (hasError) {
            return
        }

        sendAuthVerificationUseCase(phone = phone, forLogin = true).onEach {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { vId ->
                        _state.value = _state.value.copy(showCode = true)
                        verifyId = vId
                    }
                    if (sendAgain)
                        sendMessage(UIComponent.Snackbar(message = context.getString(R.string.sent_again)))
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = it.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    private suspend fun login() {
        val phone = _state.value.phone
        val code = _state.value.verifyCode.getCode()
        val vId = verifyId

        val phoneValidate = validatePhoneNumber.execute(phone)
        val codeValidate = required.execute("Code", code)


        val hasError = listOf(
            phoneValidate,
            codeValidate
        ).any { !it.successful }

        _state.value = _state.value.copy(
            phoneError = phoneValidate.errorMessage
        )

        if (hasError || vId == null) {
            Log.d(TAG, "login: error sent to UI")
            return
        }

        loginUseCase(phone = phone, verifyId = vId, code = code).onEach {
            when (it) {

                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(verifyCode = OPT4Digits(""))
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = it.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)

    }


    private fun rest() {
        _state.value = _state.value.copy(
            phone = "",
            phoneError = null,
            showCode = false,
            verifyCode = OPT4Digits("")
        )
        verifyId = null
    }


}