package com.example.ibrasaloonapp.presentation.ui.book_appointment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.core.getCurrentDateAsString
import com.example.ibrasaloonapp.core.stringToDate
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.use_case.ValidateRequired
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.AppointmentDto
import com.example.ibrasaloonapp.presentation.ui.login.LoginViewModel
import com.example.ibrasaloonapp.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BookAppointmentViewMode"

@HiltViewModel
class BookAppointmentViewModel
@Inject
constructor(
    private val repository: AppointmentRepository,
    private val validateRequired: ValidateRequired
) : ViewModel() {

    sealed class UIEvent {
        object OnBookAppointment : UIEvent()
    }

    private val _state: MutableState<BookAppointmentState> = mutableStateOf(BookAppointmentState())
    val state: State<BookAppointmentState> = _state

    private val _events = Channel<UIEvent>()
    val events = _events.receiveAsFlow()

    init {
        onTriggerEvent(BookAppointmentEvent.GetServicesTypes)
        onTriggerEvent(BookAppointmentEvent.GetAvailableAppointments)
    }

    fun onTriggerEvent(event: BookAppointmentEvent) {
        viewModelScope.launch {
            when (event) {
                is BookAppointmentEvent.DateChanged -> {
                    _state.value = _state.value.copy(
                        date = event.date,
                        time = "",
                        availableAppointmentsTimesList = listOf()
                    )
                    getAvailableAppointmentsTimes()
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
                    getAvailableAppointmentsTimes()
                }

                is BookAppointmentEvent.GetServicesTypes -> {
                    getServicesType()
                }

                is BookAppointmentEvent.OnRemoveHeadFromQueue -> {
                    removeHeadMessage()
                }
            }
        }
    }

    private suspend fun getAvailableAppointmentsTimes() {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = repository.getAvailableAppointments(stringToDate(_state.value.date))

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(availableAppointmentsTimesList = result.value)
                Log.d(TAG, "getAppointments: ${result.value}")
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


    private suspend fun getServicesType() {
        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)



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

        _state.value = _state.value.copy(progressBarState = ProgressBarState.Loading)

        val result = repository.bookAppointment(
            AppointmentDto(
//                date = stringToDate(_state.value.date),
//                time = _state.value.time,
//                type = _state.value.serviceType
            )
        )

        when (result) {
            is ApiResult.Success -> {
                onTriggerEvent(BookAppointmentEvent.GetServicesTypes)
                onTriggerEvent(BookAppointmentEvent.GetAvailableAppointments)
                _state.value =
                    _state.value.copy(serviceType = "", time = "", date = getCurrentDateAsString())
                _events.send(UIEvent.OnBookAppointment)
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Booked",
                        description = "the appointment has been booked"
                    )
                )
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "submitData: ${result.errorMessage}")
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