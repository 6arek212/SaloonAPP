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


    init {
        onTriggerEvent(MainEvent.GetAuthData)
        observeNetwork()
    }


    fun onTriggerEvent(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is MainEvent.Login -> {
                    _state.value = _state.value.copy(authData = event.authData, isLoggedIn = true)
                }

                is MainEvent.DismissNetworkMessage -> {
                    setNetworkStatus(null)
                }

                is MainEvent.GetAuthData -> {
                    checkAuth()
                }

                is MainEvent.Logout -> {
                    logout()
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


