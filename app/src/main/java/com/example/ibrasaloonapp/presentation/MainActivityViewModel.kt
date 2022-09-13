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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"


@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    val authRepository: AuthRepository
) : BaseViewModel() {

    //TODO: NETWORK STATUS !!!!
    private val _state: MutableState<AuthState> = mutableStateOf(AuthState())
    val state: State<AuthState> = _state


    init {
        onTriggerEvent(MainEvent.GetAuthData)
    }


    fun onTriggerEvent(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is MainEvent.Login -> {
                    _state.value = _state.value.copy(authData = event.authData, isLoggedIn = true)
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
            sendUiEvent(MainUIEvent.AuthDataReady(true))
        } else {
            _state.value = AuthState(isLoggedIn = false, authData = null)
            sendUiEvent(MainUIEvent.AuthDataReady(false))
        }

        Log.d(TAG, "checkAuth: ${authData}")
    }


    private suspend fun logout() {
        authRepository.logout()
        _state.value = _state.value.copy(authData = null, isLoggedIn = false)
        sendUiEvent(MainUIEvent.Logout)
    }


}


