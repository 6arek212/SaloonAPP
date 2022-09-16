package com.example.ibrasaloonapp.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.presentation.ui.UIState
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

    protected fun setNetworkStatus(state: Boolean?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(network = state)
        }
    }


    protected fun sendUiEvent(uiEvent: MainUIEvent) {
        viewModelScope.launch {
            _uiEvents.send(uiEvent)
        }
    }

    protected fun sendMessage(uiComponent: UIComponent) {
        _uiState.value = _uiState.value.copy(uiMessage = uiComponent)
    }

    protected fun removeMessage() {
        _uiState.value = _uiState.value.copy(uiMessage = null)

    }
}