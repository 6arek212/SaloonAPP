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
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.model.SignupDataDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace
import com.example.ibrasaloonapp.presentation.ui.login.LoginEvent
import com.example.ibrasaloonapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SignupViewModel"

@HiltViewModel
class SignupViewModel
@Inject
constructor(
    private val context: Application,
    private val authRepository: AuthRepository,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val required: ValidateRequired
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
                    removeHeadMessage()
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

        loading(true)

        val result = authRepository.sendAuthVerification(phone = phone)

        when (result) {
            is ApiResult.Success -> {
                this.verifyId = result.value
                _state.value = _state.value.copy(showCode = true)
            }

            is ApiResult.GenericError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = result.errorMessage
                    )
                )

                _state.value = _state.value.copy(verifyCode = OPT4Digits(""))
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
                _state.value = _state.value.copy(verifyCode = OPT4Digits(""))
            }
        }


        loading(false)
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

        loading(true)

        val result = authRepository.signup(
            SignupDataDto(
                firstName = firstName,
                lastName = lastName,
                birthDate = birthDate,
                phone = phone,
                verifyId = vId,
                code = code
            )
        )

        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "signup: signup in success")
                sendUiEvent(MainUIEvent.LoggedIn(result.value))
                nextPage()
            }

            is ApiResult.GenericError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = result.errorMessage
                    )
                )

                _state.value = _state.value.copy(verifyCode = OPT4Digits("", "", "", ""))
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
                _state.value = _state.value.copy(verifyCode = OPT4Digits("", "", "", ""))
            }
        }

        loading(false)
    }

}