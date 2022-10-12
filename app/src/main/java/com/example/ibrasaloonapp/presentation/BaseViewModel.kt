package com.example.ibrasaloonapp.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.UIMessagesController
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.presentation.ui.UIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "BaseViewModel"

open class BaseViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()


    val uiMessagesController: UIMessagesController =UIMessagesController.getInstance()


    protected fun loading(state: Boolean) {
        if (state)
            _uiState.value = _uiState.value.copy(progressBarState = ProgressBarState.Loading)
        else
            _uiState.value = _uiState.value.copy(progressBarState = ProgressBarState.Idle)
    }

    protected fun isLoading(): Boolean {
        return _uiState.value.progressBarState == ProgressBarState.Loading
    }

    protected fun setNetworkStatus(state: Boolean?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(network = state)
        }
    }


    protected suspend fun sendMessage(uiComponent: UIComponent) {
        uiMessagesController.sendMessage(uiComponent)
//        _uiState.value = _uiState.value.copy(uiMessage = uiComponent)
    }

    protected suspend fun removeMessage() {
        uiMessagesController.removeMessage()
//        _uiState.value = _uiState.value.copy(uiMessage = null)
    }
}