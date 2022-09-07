package com.example.ibrasaloonapp.presentation.ui.appointment_list

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
import com.example.ibrasaloonapp.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "AppointmentsListVM"

@HiltViewModel
class AppointmentsListViewModel
@Inject
constructor(
    private val repository: AppointmentRepository
) : ViewModel() {

    private val _state: MutableState<AppointmentListState> = mutableStateOf(AppointmentListState())
    val state: State<AppointmentListState> = _state


    init {
        onTriggerEvent(AppointmentListEvent.GetAppointments)
    }

    fun onTriggerEvent(event: AppointmentListEvent) {
        viewModelScope.launch {
            when (event) {
                is AppointmentListEvent.GetAppointments -> {
                    getAppointments()
                }
                is AppointmentListEvent.OnRemoveHeadFromQueue -> {
                    removeHeadMessage()
                }
                is AppointmentListEvent.UnBookAppointment -> {
                    unbook(id = event.id, index = event.index)
                }
            }
        }
    }

    private suspend fun getAppointments() {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = repository.getAppointments()

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(appointments = result.value)
            }

            is ApiResult.GenericError -> {

            }

            is ApiResult.NetworkError -> {

            }
        }
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Idle)
    }


    suspend fun unbook(id: String, index: Int) {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = repository.unbookAppointment(id)

        when (result) {
            is ApiResult.Success -> {
                val list = ArrayList(_state.value.appointments)
                list.removeAt(index)
                _state.value = _state.value.copy(appointments = list)
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Unbooked",
                        description = "You'r appointment has been unbooked"
                    )
                )
            }

            is ApiResult.GenericError -> {

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
            _state.value = state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
            _state.value = state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            Log.d(TAG, "removeHeadMessage: Nothing to remove from DialogQueue")
        }
    }

}