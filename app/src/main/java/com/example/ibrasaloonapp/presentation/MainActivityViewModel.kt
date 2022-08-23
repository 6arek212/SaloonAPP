package com.example.ibrasaloonapp.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.presentation.ui.login.LoginState
import com.example.ibrasaloonapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
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

    private val _state: MutableLiveData<AuthState> = MutableLiveData(AuthState())
    val state: LiveData<AuthState> = _state


    init {
        onTriggerEvent()
    }


    fun onTriggerEvent() {
        checkAuth()
    }


    private fun checkAuth() {
        viewModelScope.launch {
            val authData = authRepository.getLoginStatus()
            if (!authData.token.isBlank()) {
                _state.value = AuthState(isLoggedIn = true, authData = authData)
            } else {
                _state.value = AuthState(isLoggedIn = false)
            }

            Log.d(TAG, "checkAuth: ${authData}")
        }
    }


}


