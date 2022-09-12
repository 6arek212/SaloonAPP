package com.example.ibrasaloonapp.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"


@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    val authRepository: AuthRepository
) : ViewModel() {

    //TODO: NETWORK STATUS !!!!
    private val _state: MutableState<AuthState> = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    private val _events = Channel<UIEvent>()
    val events = _events.receiveAsFlow()

    init {
        onTriggerEvent(MainEvent.GetAuthData)
    }


    fun onTriggerEvent(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is MainEvent.Login -> {
                    _state.value = _state.value.copy(authData = event.authData)
                }

                is MainEvent.GetAuthData -> {
                    checkAuth()
                }

                is MainEvent.Logout -> {
                    logout()
                }
            }
        }
    }


    private suspend fun checkAuth() {
        val authData = authRepository.getLoginStatus()

        if (authData != null) {
            _state.value = state.value.copy(isLoggedIn = true, authData = authData)
            _events.send(UIEvent.NavigateNow(Screen.AppointmentsList.route))
        } else {
            _state.value = AuthState(isLoggedIn = false)
            _events.send(UIEvent.NavigateNow(Screen.Login.route))
        }

        Log.d(TAG, "checkAuth: ${authData}")
    }


    private suspend fun logout() {
        authRepository.logout()
        _state.value = _state.value.copy(authData = null)
    }

    sealed class UIEvent {
        class NavigateNow(val route: String) : UIEvent()
    }


}


