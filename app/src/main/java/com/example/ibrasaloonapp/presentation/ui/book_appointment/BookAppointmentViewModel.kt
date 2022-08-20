package com.example.ibrasaloonapp.presentation.ui.book_appointment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.domain.model.ProgressBarState
import com.example.ibrasaloonapp.domain.user_case.ValidateRequired
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BookAppointmentViewMode"

@HiltViewModel
class BookAppointmentViewModel
@Inject
constructor(
    val repository: SessionRepository,
    val validateRequired: ValidateRequired
) : ViewModel() {

    private val _state: MutableState<BookAppointmentState> = mutableStateOf(BookAppointmentState())
    val state: State<BookAppointmentState> = _state


    init {
        onTriggerEvent(BookAppointmentEvent.GetAvailableAppointments)
    }

    fun onTriggerEvent(event: BookAppointmentEvent) {
        viewModelScope.launch {
            when (event) {
                is BookAppointmentEvent.DateChanged -> {
                    _state.value = _state.value.copy(date = event.date)
                }
                is BookAppointmentEvent.ServiceTypeChanged -> {
                    _state.value = _state.value.copy(serviceType = event.type)
                }

                is BookAppointmentEvent.TimeChanged -> {
                    _state.value = _state.value.copy(time = event.time)
                }

                is BookAppointmentEvent.ServiceTypeDropDownExpandChange -> {
                    _state.value = _state.value.copy(expandDropDown1 = event.value)
                }

                is BookAppointmentEvent.TimeDropDownExpandChange -> {
                    _state.value = _state.value.copy(expandDropDown2 = event.value)
                }


                is BookAppointmentEvent.Submit -> {
                    submitData()
                }
                is BookAppointmentEvent.GetAvailableAppointments -> {
                    getAppointments()
                    getServicesType()
                }
            }
        }
    }

    private suspend fun getAppointments() {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = repository.getAvailableAppointments()

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(appointmentsList = result.value)
                Log.d(TAG, "getAppointments: ${result.value}")
            }

            is ApiResult.GenericError -> {

            }

            is ApiResult.NetworkError -> {

            }
        }


        _state.value = _state.value.copy(progressBarState = ProgressBarState.Idle)
    }


    private suspend fun getServicesType() {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = repository.getServiceType()

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(typesList = result.value)
            }

            is ApiResult.GenericError -> {

            }

            is ApiResult.NetworkError -> {

            }
        }

        _state.value = _state.value.copy(progressBarState = ProgressBarState.Idle)
    }


    private suspend fun submitData() {
        val dateResult = validateRequired.execute("date", state.value.date)
        val timeResult = validateRequired.execute(fieldName = "time", state.value.time)
        val typeResult = validateRequired.execute(fieldName = "type", state.value.serviceType)

        val hasError = listOf(
            dateResult,
            timeResult,
            typeResult
        ).any { !it.successful }

        _state.value = _state.value.copy(
            dateError = dateResult.errorMessage,
            timeError = timeResult.errorMessage,
            serviceTypeError = typeResult.errorMessage
        )

        if (hasError) {
            Log.d(TAG, "submitData: error sent to UI")
            return
        }
    }


}