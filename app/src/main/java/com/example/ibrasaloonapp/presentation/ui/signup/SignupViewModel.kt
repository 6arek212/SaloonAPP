package com.example.ibrasaloonapp.presentation.ui.signup

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.domain.use_case.ValidatePhoneNumber
import com.example.ibrasaloonapp.domain.use_case.ValidateRequired
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.SignupDataDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.ibrasaloonapp.use.SendAuthVerificationUseCase
import com.example.ibrasaloonapp.use.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignupViewModel"

@HiltViewModel
class SignupViewModel
@Inject
constructor(
    private val context: Application,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val required: ValidateRequired,
    private val sendAuthVerificationUseCase: SendAuthVerificationUseCase,
    private val signupUseCase: SignupUseCase
) : BaseViewModel() {

    private val _state: MutableState<SignupState> = mutableStateOf(SignupState())
    val state: State<SignupState> = _state

    val pagesNumber: Int = 4
    private var verifyId: String? = null

    fun onTriggerEvent(event: SignupEvent) {
        viewModelScope.launch {
            when (event) {

                is SignupEvent.UpdateImage -> {
                    _state.value = _state.value.copy(image = event.imagePath)
                }

                is SignupEvent.OnPhoneChanged -> {
                    _state.value = _state.value.copy(phone = event.phone)
                }

                is SignupEvent.OnBirthDateChanged -> {
                    _state.value = _state.value.copy(birthDate = event.birthdate)
                }
                is SignupEvent.OnFirstNameChanged -> {
                    _state.value = _state.value.copy(firstName = event.name)
                }

                is SignupEvent.OnLastNameChanged -> {
                    _state.value = _state.value.copy(lastName = event.last)
                }

                is SignupEvent.OnCodeDigitChanged -> {
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
                            signup()
                        }
                    }
                }

                is SignupEvent.Signup -> {
                    signup()
                }

                is SignupEvent.NextPage -> {
                    nextPage()
                }

                is SignupEvent.PrevPage -> {
                    prevPage()
                }

                is SignupEvent.SendAuthVerification -> {
                    sendAuthVerification()
                }

                is SignupEvent.OnRemoveHeadFromQueue -> {
                    removeMessage()
                }
            }

        }

    }

    private fun nextPage() {
        val page = _state.value.page
        if (page < pagesNumber)
            _state.value = _state.value.copy(page = page + 1)
    }

    private fun prevPage() {
        val page = _state.value.page
        if (page > 1 && page != pagesNumber)
            _state.value = _state.value.copy(page = page - 1)
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

        sendAuthVerificationUseCase(phone = phone).onEach {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { vId ->
                        _state.value = _state.value.copy(showCode = true)
                        verifyId = vId
                    }
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


    private suspend fun signup() {
        val firstName = _state.value.firstName
        val lastName = _state.value.lastName
        val phone = _state.value.phone
        val birthDate = _state.value.birthDate
        val code = _state.value.verifyCode.getCode()
        val vId = verifyId


        val firstNameValidate = required.execute("First Name", firstName)
        val lastNameValidate = required.execute("Last Name", lastName)
        val birthDateValidate = required.execute("Birth Date", birthDate)
        val phoneValidate = validatePhoneNumber.execute(phone)
        val codeValidate = required.execute("Code", code)


        val hasError = listOf(
            firstNameValidate,
            lastNameValidate,
            birthDateValidate,
            phoneValidate,
            codeValidate
        ).any { !it.successful }

        _state.value = _state.value.copy(
            phoneError = phoneValidate.errorMessage,
            firstNameError = firstNameValidate.errorMessage,
            lastNameError = lastNameValidate.errorMessage,
            birthDateError = birthDateValidate.errorMessage,
        )

        if (hasError || vId == null) {
            Log.d(TAG, "signup: error sent to UI")
            return
        }


        signupUseCase(
            SignupDataDto(
                firstName = firstName,
                lastName = lastName,
                birthDate = birthDate,
                phone = phone,
                verifyId = vId,
                code = code
            )
        ).onEach { data ->
            when (data) {
                is Resource.Loading -> {
                    loading(data.value)
                }

                is Resource.Success -> {
                    nextPage()
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = data.message
                        )
                    )
                    _state.value = _state.value.copy(verifyCode = OPT4Digits(""))

                }
            }
        }.launchIn(viewModelScope)
    }

}