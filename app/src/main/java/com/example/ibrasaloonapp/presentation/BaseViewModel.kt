package com.example.ibrasaloonapp.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.presentation.ui.UIState
import com.example.ibrasaloonapp.presentation.ui.login.LoginViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


private const val TAG = "BaseViewModel"

open class BaseViewModel : ViewModel() {
    private val _uiState: MutableState<UIState> = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    private val _uiEvents = Channel<MainUIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()


    protected fun loading(state: Boolean) {
        if (state)
            _uiState.value = _uiState.value.copy(progressBarState = ProgressBarState.Loading)
        else
            _uiState.value = _uiState.value.copy(progressBarState = ProgressBarState.Idle)
    }

    protected fun sendUiEvent(uiEvent: MainUIEvent){
        viewModelScope.launch {
            _uiEvents.send(uiEvent)
        }
    }

    protected fun appendToMessageQueue(uiComponent: UIComponent) {
        val queue = _uiState.value.errorQueue
        queue.add(uiComponent)
        _uiState.value = _uiState.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
        _uiState.value = _uiState.value.copy(errorQueue = queue)
    }

    protected fun removeHeadMessage() {
        try {
            val queue = _uiState.value.errorQueue
            queue.remove() // can throw exception if empty
            _uiState.value =
                _uiState.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
            _uiState.value = _uiState.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            Log.d(TAG, "removeHeadMessage: Nothing to remove from DialogQueue")
        }
    }
}