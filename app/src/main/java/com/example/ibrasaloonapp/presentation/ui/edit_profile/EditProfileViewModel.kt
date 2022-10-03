package com.example.ibrasaloonapp.presentation.ui.edit_profile

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.domain.use_case.ValidateName
import com.example.ibrasaloonapp.domain.use_case.ValidatePhoneNumber
import com.example.ibrasaloonapp.domain.use_case.ValidateRequired
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace
import com.example.ibrasaloonapp.presentation.ui.upload.EditProfileUIEvent
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.use.SendAuthVerificationUseCase
import com.example.ibrasaloonapp.use.UpdatePhoneUseCase
import com.example.ibrasaloonapp.use.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "EditProfileViewModel"

@HiltViewModel
class EditProfileViewModel
@Inject
constructor(
    private val context: Application,
    private val savedState: SavedStateHandle,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updatePhoneUseCase: UpdatePhoneUseCase,
    private val sendAuthVerificationUseCase: SendAuthVerificationUseCase,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val nameValidator: ValidateName,
    private val required: ValidateRequired,
    private val authRepository: AuthRepository
) : BaseViewModel() {


    private val _state: MutableState<EditProfileState> = mutableStateOf(EditProfileState())
    val state: State<EditProfileState> = _state


    private val _events = Channel<EditProfileUIEvent>()
    val events = _events.receiveAsFlow()


    private var verifyId: String? = null

    init {
        val userId = savedState.get<String>("userId")
        val firstName = savedState.get<String>("firstName")
        val lastName = savedState.get<String>("lastName")
        val phone = savedState.get<String>("phone")

        if (userId != null && firstName != null && lastName != null && phone != null) {
            _state.value = _state.value.copy(
                userId = userId,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
            )
        }
    }

    fun onTriggerEvent(event: EditProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is EditProfileEvent.OnFirstNameChanged -> {
                    val firstNameValidate =
                        nameValidator.execute(context.getString(R.string.first_name), event.name)
                    _state.value = _state.value.copy(
                        firstName = event.name,
                        firstNameError = firstNameValidate.errorMessage
                    )
                }

                is EditProfileEvent.OnLastNameChanged -> {
                    val lastNameValidate =
                        nameValidator.execute(context.getString(R.string.last_name), event.last)
                    _state.value = _state.value.copy(
                        lastName = event.last,
                        lastNameError = lastNameValidate.errorMessage
                    )
                }

                is EditProfileEvent.OnRestCode -> {
                    _state.value = _state.value.copy(code = OPT4Digits(""), showCode = false)
                }

                is EditProfileEvent.OnPhoneChanged -> {
                    val phoneValidate = validatePhoneNumber.execute(event.phone)
                    _state.value = _state.value.copy(
                        phone = event.phone,
                        phoneError = phoneValidate.errorMessage
                    )
                }

                is EditProfileEvent.OnCodeDigitChanged -> {
                    Log.d(TAG, "onTriggerEvent: oncode change")
                    removeMessage()
                    when (event.codePlace) {
                        CodeDigitPlace.ONE -> {
                            val code = _state.value.code.copy(one = event.value)
                            _state.value = _state.value.copy(code = code)
                        }
                        CodeDigitPlace.TWO -> {
                            val code = _state.value.code.copy(two = event.value)
                            _state.value = _state.value.copy(code = code)
                        }
                        CodeDigitPlace.THREE -> {
                            val code = _state.value.code.copy(three = event.value)
                            _state.value = _state.value.copy(code = code)
                        }
                        CodeDigitPlace.FOUR -> {
                            val code = _state.value.code.copy(four = event.value)
                            _state.value = _state.value.copy(code = code)
                            updatePhone()
                        }
                    }
                }

                is EditProfileEvent.UpdateProfile -> {
                    updateProfile()
                }

                is EditProfileEvent.OnRemoveHeadFromQueue -> {
                    removeMessage()
                }

                is EditProfileEvent.SendAuthVerification -> {
                    sendAuthVerification(event.sendAgain)
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

        sendAuthVerificationUseCase(phone = phone, forSignup = true).onEach {
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


    private suspend fun updatePhone() {
        val phone = state.value.phone
        val userId = authRepository.getUserId() ?: return
        val verifyId = this.verifyId ?: return
        val code = _state.value.code.getCode()
        val phoneValidate = validatePhoneNumber.execute(phone)

        if (!phoneValidate.successful) {
            _state.value = _state.value.copy(phoneError = phoneValidate.errorMessage)
            return
        }

        updatePhoneUseCase(
            phone = phone,
            userId = userId,
            verifyId = verifyId,
            code = code
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.updated),
                            context.getString(R.string.your_profile_has_been_updated)
                        )
                    )
                    _events.send(EditProfileUIEvent.PhoneUpdated)
                    rest()
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

    private fun validateInfo(): Boolean {
        val firstName = _state.value.firstName
        val lastName = _state.value.lastName
//        val birthDate = _state.value.birthDate


        val firstNameValidate =
            nameValidator.execute(context.getString(R.string.first_name), firstName)
        val lastNameValidate =
            nameValidator.execute(context.getString(R.string.last_name), lastName)
//        val birthDateValidate = required.execute(context.getString(R.string.birth_date), birthDate)


        _state.value = _state.value.copy(
            firstNameError = firstNameValidate.errorMessage,
            lastNameError = lastNameValidate.errorMessage,
//            birthDateError = birthDateValidate.errorMessage,
        )


        return !listOf(
            firstNameValidate,
            lastNameValidate,
//            birthDateValidate
        ).any { !it.successful }
    }

    private suspend fun updateProfile() {
        val firstName = state.value.firstName
        val lastName = state.value.lastName
        val userId = authRepository.getUserId() ?: return
        //need validation !

        if (!validateInfo()) {
            return
        }

        updateProfileUseCase(
            firstName = firstName,
            lastName = lastName,
            userId = userId
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { user ->
                        sendMessage(
                            UIComponent.Dialog(
                                title = context.getString(R.string.updated),
                                context.getString(R.string.your_profile_has_been_updated)
                            )
                        )
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


    private fun rest() {
        this.verifyId = null
        _state.value = _state.value.copy(showCode = false, code = OPT4Digits(""), phoneError = null)
    }
}