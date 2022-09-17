package com.example.ibrasaloonapp.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.utils.ConnectivityObserver
import com.example.ibrasaloonapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"


@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val networkConnectivityObserver: ConnectivityObserver,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _state: MutableState<AuthState> = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    private val _uiEvents = Channel<MainUIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        onTriggerEvent(MainEvent.GetAuthData)
        observeNetwork()
        observeAuthStatus()
    }


    fun onTriggerEvent(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is MainEvent.DismissNetworkMessage -> {
                    setNetworkStatus(null)
                }

                is MainEvent.GetAuthData -> {
                    checkAuth()
                }

                is MainEvent.Logout -> {
                    authRepository.logout()
                }

                is MainEvent.ShowLogoutDialog -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = "Logout",
                            "Are you sure you want to logout?",
                            actionButtons = true
                        )
                    )
                }

                is MainEvent.RemoveMessage -> {
                    removeMessage()
                }

            }
        }
    }


    private fun sendUiEvent(uiEvent: MainUIEvent) {
        viewModelScope.launch {
            _uiEvents.send(uiEvent)
        }
    }

    private suspend fun checkAuth() {
        val authData = authRepository.getCacheAuthData()
        if (authData != null) {
            _state.value = state.value.copy(isLoggedIn = true, authData = authData)
            sendUiEvent(MainUIEvent.AuthDataReady)
        } else {
            _state.value = AuthState(isLoggedIn = false, authData = null)
            sendUiEvent(MainUIEvent.AuthDataReady)
        }
    }


    private fun observeAuthStatus() {
        viewModelScope.launch {
            authRepository.getAuthFlow().collect() { data ->
                when (data) {
                    is AuthEvent.Logout -> {
                        sendUiEvent(MainUIEvent.Logout)
                        _state.value = state.value.copy(isLoggedIn = false, authData = null)
                    }

                    is AuthEvent.Login -> {
                        _state.value = state.value.copy(isLoggedIn = true, authData = data.authData)
                        sendUiEvent(MainUIEvent.LoggedIn)
                    }

                    is AuthEvent.Nothing -> {

                    }
                }
            }
        }
    }


    private fun observeNetwork() {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect() {
                when (it) {

                    ConnectivityObserver.Status.Available -> {
                        Log.d(TAG, "observeNetwork: 1")
                        val hasNetwork = uiState.value.network

                        if (hasNetwork != null && !hasNetwork)
                            setNetworkStatus(true)
                    }

                    ConnectivityObserver.Status.Lost -> {
                        Log.d(TAG, "observeNetwork: 2")

                        setNetworkStatus(false)
                    }

                    ConnectivityObserver.Status.Unavailable -> {
                        Log.d(TAG, "observeNetwork: 3")

                        setNetworkStatus(false)
                    }

                    ConnectivityObserver.Status.Losing -> {
                        Log.d(TAG, "observeNetwork: 4")

                        setNetworkStatus(false)
                    }
                }

            }
        }
    }


}


