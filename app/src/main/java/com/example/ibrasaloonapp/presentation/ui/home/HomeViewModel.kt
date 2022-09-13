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
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.MainUIEvent
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
) : BaseViewModel() {

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
        loading(true)

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

                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }

            }

            is ApiResult.NetworkError -> {

            }
        }


        loading(false)
    }


    private suspend fun getWorkers() {
        loading(true)

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


        loading(false)
    }


}