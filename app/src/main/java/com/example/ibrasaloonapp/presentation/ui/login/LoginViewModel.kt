package com.example.ibrasaloonapp.presentation.ui.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.core.getCurrentDateAsString
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.domain.use_case.ValidatePassword
import com.example.ibrasaloonapp.domain.use_case.ValidatePhoneNumber
import com.example.ibrasaloonapp.domain.use_case.ValidateRequired
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.ui.book_appointment.BookAppointmentEvent
import com.example.ibrasaloonapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val required: ValidateRequired
) : BaseViewModel() {

    private val _state: MutableState<LoginState> = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    private val _events = Channel<UIEvent>()
    val events = _events.receiveAsFlow()

    private var verifyId: String? = null


    init {
//        onTriggerEvent(LoginEvent.LoggedInStatus)
    }


    fun onTriggerEvent(event: LoginEvent) {
        viewModelScope.launch {
            when (event) {
                is LoginEvent.OnPhoneValueChange -> {
                    _state.value = _state.value.copy(phone = event.value)
                }

                is LoginEvent.OnCodeDigitChanged -> {
                    when (event.codePlace) {
                        CodeDigitPlace.ONE -> {
                            _state.value = _state.value.copy(
                                verifyCode = OPT4Digits(
                                    one = event.value,
                                    two = _state.value.verifyCode.two,
                                    three = _state.value.verifyCode.three,
                                    four = _state.value.verifyCode.four,
                                )
                            )
                        }
                        CodeDigitPlace.TWO -> {
                            _state.value = _state.value.copy(
                                verifyCode = OPT4Digits(
                                    one = _state.value.verifyCode.one,
                                    two = event.value,
                                    three = _state.value.verifyCode.three,
                                    four = _state.value.verifyCode.four,
                                )
                            )
                        }
                        CodeDigitPlace.THREE -> {
                            _state.value = _state.value.copy(
                                verifyCode = OPT4Digits(
                                    one = _state.value.verifyCode.one,
                                    two = _state.value.verifyCode.two,
                                    three = event.value,
                                    four = _state.value.verifyCode.four,
                                )
                            )
                        }
                        CodeDigitPlace.FOUR -> {
                            _state.value = _state.value.copy(
                                verifyCode = OPT4Digits(
                                    one = _state.value.verifyCode.one,
                                    two = _state.value.verifyCode.two,
                                    three = _state.value.verifyCode.three,
                                    four = event.value,
                                )
                            )
                            login()
                        }
                    }


                }

                is LoginEvent.Reset -> {
                    _state.value = _state.value.copy(
                        verifyCode = OPT4Digits("", "", "", ""),
                        showCode = false
                    )
                }

                is LoginEvent.SendAuthVerification -> {
                    sendAuthVerification()
                }

                is LoginEvent.OnRemoveHeadFromQueue -> {
                    removeHeadMessage()
                }

                is LoginEvent.LoggedInStatus -> {
                    checkLoginStatus()
                }

                is LoginEvent.Login -> {
                    login()
                }
            }
        }
    }


    private suspend fun checkLoginStatus() {
//        authRepository.getLoginStatus().collect() {
//            if (!it.token.isBlank()) {
//                _events.send(UIEvent.LoggedIn)
//            }
//        }
    }


    private suspend fun sendAuthVerification() {
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

        loading(true)

        val result = authRepository.sendAuthVerification(phone = phone)

        when (result) {
            is ApiResult.Success -> {
                this.verifyId = result.value
                _state.value = _state.value.copy(showCode = true)
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "sendAuthVerification: ")
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = result.errorMessage
                    )
                )

                _state.value = _state.value.copy(verifyCode = OPT4Digits("", "", "", ""))
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = "Something went wrong"
                    )
                )
                _state.value = _state.value.copy(verifyCode = OPT4Digits("", "", "", ""))
            }
        }



        loading(false)
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

        loading(true)

        val result = authRepository.login(
            LoginDataDto(
                phone = phone,
                verifyId = vId,
                code = code
            )
        )

        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "login: logged in success")
                _events.send(UIEvent.LoggedIn)
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "login: logged in fail ${result.code}")
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = result.errorMessage
                    )
                )
                _state.value = _state.value.copy(verifyCode = OPT4Digits("", "", "", ""))
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = "Something went wrong"
                    )
                )
                _state.value = _state.value.copy(verifyCode = OPT4Digits("", "", "", ""))
            }
        }

        loading(false)
    }


    sealed class UIEvent {
        object LoggedIn : UIEvent()
    }

}