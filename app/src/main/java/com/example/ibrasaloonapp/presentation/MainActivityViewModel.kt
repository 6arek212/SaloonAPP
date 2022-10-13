package com.example.ibrasaloonapp.presentation

import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.DialogEvent
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.MenuItem
import com.example.ibrasaloonapp.network.utils.ConnectivityObserver
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    private val _uiEvents = MutableStateFlow<MainUIEvent>(MainUIEvent.Nothing)
    val uiEvents: StateFlow<MainUIEvent> = _uiEvents

    init {
        onTriggerEvent(MainEvent.GetAuthData)
        observeNetwork()
        observeAuthStatus()
        observeUIMessages()
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
                    _state.value = _state.value.copy(uiMessage = null)
                }
            }
        }
    }


    fun getDrawerItems(): List<MenuItem> {
        val list = ArrayList<MenuItem>()
        list.add(
            MenuItem(
                id = Screen.Home.route,
                title = context.getString(R.string.home),
                contentDescription = "Go to home",
                icon = Icons.Filled.Home
            )
        )
        list.add(
            MenuItem(
                id = Screen.AppointmentsList.route,
                title = context.getString(R.string.appointments),
                contentDescription = "Go to appointments",
                icon = Icons.Filled.BookOnline
            )
        )
        list.add(
            MenuItem(
                id = Screen.Profile.route,
                title = context.getString(R.string.profile),
                contentDescription = "Go to profile",
                icon = Icons.Filled.AccountBox
            )
        )

        if (_state.value.workerMode) {
            list.add(
                MenuItem(
                    id = Screen.WorkerAppointmentsList.route,
                    title = context.getString(R.string.worker_page),
                    contentDescription = "Worker Page",
                    icon = Icons.Filled.WorkOutline
                )
            )


            list.add(
                MenuItem(
                    id = Screen.UsersList.route,
                    title = context.getString(R.string.users),
                    contentDescription = "Users Page",
                    icon = Icons.Filled.Diversity3
                )
            )
        }

        list.add(
            MenuItem(
                id = "logout",
                title = context.getString(R.string.logut),
                contentDescription = "logout",
                icon = Icons.Filled.ExitToApp
            )
        )


        return list
    }


    private fun observeUIMessages() {
        viewModelScope.launch {
            uiMessagesController.message.collect { uiComponent ->
                Log.d(TAG, "observeUIMessages: $uiComponent")
                uiComponent?.let {
                    _state.value = _state.value.copy(uiMessage = uiComponent)
                }
            }
        }
    }

    private suspend fun checkAuth() {
        val authData = authRepository.getCacheAuthData(updateStatus = true)
        if (authData != null) {
            val isWorkerMode = authData.user.role == "barber"

            Log.d(TAG, "checkAuth: - ---  ${authData.user.role} ${isWorkerMode} ")

            _state.value =
                state.value.copy(isLoggedIn = true, authData = authData, workerMode = isWorkerMode)
        } else {
            _state.value = AuthState(isLoggedIn = false, authData = null, workerMode = false)
        }
        _uiEvents.emit(MainUIEvent.AuthDataReady)
        Log.d(TAG, "checkAuth: emit(MainUIEvent.AuthDataReady)")
    }


    private fun observeAuthStatus() {
        viewModelScope.launch {
            authRepository.getAuthFlow().collect() { data ->
                when (data) {
                    is AuthEvent.Logout -> {
                        _uiEvents.emit(MainUIEvent.Logout)
                        _state.value = state.value.copy(isLoggedIn = false, authData = null)
                    }

                    is AuthEvent.Login -> {
                        Log.d(TAG, "observeAuthStatus: Login ${data.authData}")
                        _state.value = state.value.copy(
                            isLoggedIn = true,
                            authData = data.authData,
                            workerMode = data.authData.user.role == "barber"
                        )
                        _uiEvents.emit(MainUIEvent.LoggedIn)
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

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }

}


