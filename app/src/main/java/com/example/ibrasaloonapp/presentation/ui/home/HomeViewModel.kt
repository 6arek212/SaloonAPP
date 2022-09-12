package com.example.ibrasaloonapp.presentation.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.presentation.ui.login.LoginEvent
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.repository.WorkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val appointmentsRepository: AppointmentRepository,
    private val workerRepository: WorkerRepository
) : ViewModel() {

    private val _state: MutableState<HomeState> = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        onTriggerEvent(HomeEvent.GetAppointment)
        onTriggerEvent(HomeEvent.GetWorkers)
    }

    fun onTriggerEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.DismissLoginDialog -> {
                    _state.value = _state.value.copy(showLoginDialog = false)
                }

                is HomeEvent.ShowLoginDialog -> {
                    _state.value = _state.value.copy(showLoginDialog = true)
                }

                is HomeEvent.OnRemoveHeadFromQueue -> {
                    removeHeadMessage()
                }

                is HomeEvent.GetAppointment -> {
                    getAppointment()
                }

                is HomeEvent.GetWorkers -> {
                    getWorkers()
                }

                is HomeEvent.UpdateAppointment -> {
                    _state.value = _state.value.copy(appointment = event.appointment)
                }

                is HomeEvent.Refresh -> {
                    _state.value = _state.value.copy(refreshing = true)
                    getAppointment()
                    getWorkers()
                    _state.value = _state.value.copy(refreshing = false)
                }
            }

        }
    }


    private suspend fun getAppointment() {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = appointmentsRepository.getAppointment()

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(appointment = result.value)
                Log.d(TAG, "getAppointment: ${result.value}")
            }

            is ApiResult.GenericError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = result.errorMessage
                    )
                )
            }

            is ApiResult.NetworkError -> {

            }
        }


        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)
    }


    private suspend fun getWorkers() {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)


        val result = workerRepository.getWorkers()

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(workers = result.value)
                Log.d(TAG, "getWorkers: ${result.value}")
            }

            is ApiResult.GenericError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = result.errorMessage
                    )
                )
            }

            is ApiResult.NetworkError -> {

            }
        }


        _state.value = _state.value.copy(progressBarState = ProgressBarState.Idle)
    }


    private fun appendToMessageQueue(uiComponent: UIComponent) {
        val queue = state.value.errorQueue
        queue.add(uiComponent)
        _state.value = _state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
        _state.value = _state.value.copy(errorQueue = queue)
    }

    private fun removeHeadMessage() {
        try {
            val queue = _state.value.errorQueue
            queue.remove() // can throw exception if empty
            _state.value = _state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
            _state.value = _state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            Log.d(TAG, "removeHeadMessage: Nothing to remove from DialogQueue")
        }
    }

}