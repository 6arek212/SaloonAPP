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
import com.example.ibrasaloonapp.domain.use_case.ValidatePassword
import com.example.ibrasaloonapp.domain.use_case.ValidatePhoneNumber
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
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
    private val validatePassword: ValidatePassword,
    private val validatePhoneNumber: ValidatePhoneNumber
) : ViewModel() {

    private val _state: MutableState<LoginState> = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    private val _events = Channel<UIEvent>()
    val events = _events.receiveAsFlow()


    init {
//        onTriggerEvent(LoginEvent.LoggedInStatus)
    }

    fun onTriggerEvent(event: LoginEvent) {
        viewModelScope.launch {
            when (event) {
                is LoginEvent.OnPhoneValueChange -> {
                    _state.value = _state.value.copy(phone = event.value)
                }

                is LoginEvent.OnPasswordValueChange -> {
                    _state.value = _state.value.copy(password = event.value)
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


    private suspend fun login() {
        val phoneValidate = validatePhoneNumber.execute(_state.value.phone)
        val passwordValidate = validatePassword.execute(_state.value.password)

        val hasError = listOf(
            phoneValidate,
            passwordValidate,
        ).any { !it.successful }

        _state.value = _state.value.copy(
            phoneError = phoneValidate.errorMessage,
            passwordError = passwordValidate.errorMessage,
        )

        if (hasError) {
            Log.d(TAG, "login: error sent to UI")
            return
        }

        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = authRepository.login(
            LoginDataDto(
                phone = _state.value.phone,
                password = _state.value.password
            )
        )

        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "login: logged in success")
                _events.send(UIEvent.LoggedIn)
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "login: logged in fail")
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = result.errorMessage
                    )
                )
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = "Something went wrong"
                    )
                )
            }
        }

        _state.value = _state.value.copy(progressBarState = ProgressBarState.Idle)
    }


    private fun appendToMessageQueue(uiComponent: UIComponent) {
        val queue = state.value.errorQueue
        queue.add(uiComponent)
        _state.value = _state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
        _state.value = _state.value.copy(errorQueue = queue)
    }

    private fun removeHeadMessage() {
        try {
            val queue = _state.value.errorQueue
            queue.remove() // can throw exception if empty
            _state.value = _state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
            _state.value = _state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            Log.d(TAG, "removeHeadMessage: Nothing to remove from DialogQueue")
        }
    }


    sealed class UIEvent {
        object LoggedIn : UIEvent()
    }

}