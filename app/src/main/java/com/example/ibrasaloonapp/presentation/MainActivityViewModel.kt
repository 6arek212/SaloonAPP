package com.example.ibrasaloonapp.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.login.LoginState
import com.example.ibrasaloonapp.presentation.ui.login.LoginViewModel
import com.example.ibrasaloonapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        onTriggerEvent()
    }


    fun onTriggerEvent() {
        checkAuth()
    }


    private fun checkAuth() {
        viewModelScope.launch {
            val authData = authRepository.getLoginStatus()
//            delay(3000L)
            if (authData.token.isNotBlank()) {
                _state.value = state.value.copy(isLoggedIn = true, authData = authData)
                _events.send(UIEvent.NavigateNow(Screen.AppointmentsList.route))
            } else {
                _state.value = AuthState(isLoggedIn = false)
                _events.send(UIEvent.NavigateNow(Screen.Login.route))
            }

            Log.d(TAG, "checkAuth: ${authData}")
        }
    }

    sealed class UIEvent {
        class NavigateNow(val route: String) : UIEvent()
    }


}


