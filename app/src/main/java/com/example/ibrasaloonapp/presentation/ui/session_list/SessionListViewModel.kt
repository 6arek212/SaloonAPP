package com.example.ibrasaloonapp.presentation.ui.session_list

import android.se.omapi.Session
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.domain.model.ProgressBarState
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


private const val TAG = "SessionListViewModel"

@HiltViewModel
class SessionListViewModel
@Inject
constructor(
    private val repository: SessionRepository
) : ViewModel() {

    private val _state: MutableState<SessionListState> = mutableStateOf(SessionListState())
    val state: State<SessionListState> = _state


    init {
        onTriggerEvent(SessionListEvent.GetSessions)
    }

    fun onTriggerEvent(event: SessionListEvent) {
        viewModelScope.launch {
            when (event) {
                is SessionListEvent.GetSessions -> {
                    getSessions()
                }
                is SessionListEvent.OnRemoveHeadFromQueue -> {
                    removeHeadMessage()
                }
            }
        }
    }


    suspend fun getSessions() {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = repository.getSessions()

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(sessions = result.value)
            }

            is ApiResult.GenericError -> {

            }

            is ApiResult.NetworkError -> {

            }
        }

        _state.value = _state.value.copy(progressBarState = ProgressBarState.Idle)
    }


    private fun removeHeadMessage() {
        try {
            val queue = _state.value.errorQueue
            queue.remove() // can throw exception if empty
            _state.value = state.value.copy(errorQueue = LinkedList()) // force recompose
            _state.value = state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            Log.d(TAG, "removeHeadMessage: Nothing to remove from DialogQueue")
        }
    }

}