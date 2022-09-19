package com.example.ibrasaloonapp.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.DialogEvent
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.utils.ConnectivityObserver
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.UserRepository
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
    private val authRepository: AuthRepository,
    private val context: Application
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
                            title = context.getString(R.string.logut),
                            description = context.getString(R.string.are_you_sure_you_want_to_logout),
                            actionButtons = true,
                            dialogEvent = DialogEvent.Logout
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
        val authData = authRepository.getCacheAuthData(updateStatus = true)
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
                        Log.d(TAG, "observeAuthStatus: Login ${data.authData}")
                        _state.value = state.value.copy(
                            isLoggedIn = true,
                            authData = data.authData
                        )
                        sendUiEvent(MainUIEvent.LoggedIn)
                    }

                    is AuthEvent.UpdateUser -> {
                        val authData = _state.value.authData?.copy(user = data.user)
                        _state.value = state.value.copy(authData = authData)
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


