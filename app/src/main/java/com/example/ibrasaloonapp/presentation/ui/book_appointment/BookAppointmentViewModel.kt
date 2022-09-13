package com.example.ibrasaloonapp.presentation.ui.book_appointment

import android.app.Application
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
import com.example.ibrasaloonapp.core.getCurrentDateAsString
import com.example.ibrasaloonapp.core.getDateAsString
import com.example.ibrasaloonapp.domain.use_case.ValidateRequired
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.BookAppointmentDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.repository.WorkerRepository
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
    private val context: Application,
    private val repository: AppointmentRepository,
    private val workerRepository: WorkerRepository,
    private val validateRequired: ValidateRequired,
    private val application: Application
) : BaseViewModel() {


    private val _state: MutableState<BookAppointmentState> = mutableStateOf(BookAppointmentState())
    val state: State<BookAppointmentState> = _state

    private val _events = Channel<BookAppointmentUIEvent>()
    val events = _events.receiveAsFlow()

    init {
        onTriggerEvent(BookAppointmentEvent.GetWorkers)
    }

    fun onTriggerEvent(event: BookAppointmentEvent) {
        viewModelScope.launch {
            when (event) {
                is BookAppointmentEvent.OnSelectedWorker -> {
                    _state.value = _state.value.copy(
                        selectedWorker = event.worker,
                        selectedWorkingDate = null,
                        selectedService = "",
                        selectedAppointment = null,
                        workingDates = listOf(),
                        services = listOf(),
                        availableAppointments = listOf()
                    )
                    onTriggerEvent(BookAppointmentEvent.GetWorkingDates)
                    _events.send(BookAppointmentUIEvent.HideSheet)
                }
                is BookAppointmentEvent.OnSelectedWorkingDate -> {
                    _state.value = _state.value.copy(
                        selectedWorkingDate = event.date,
                        selectedService = "",
                        selectedAppointment = null,
                        services = listOf(),
                        availableAppointments = listOf()
                    )
                    onTriggerEvent(BookAppointmentEvent.GetServices)
                    _events.send(BookAppointmentUIEvent.HideSheet)
                }
                is BookAppointmentEvent.OnSelectedService -> {
                    _state.value = _state.value.copy(
                        selectedService = event.service,
                        selectedAppointment = null,
                        availableAppointments = listOf()
                    )
                    onTriggerEvent(BookAppointmentEvent.GetAvailableAppointments)
                    _events.send(BookAppointmentUIEvent.HideSheet)
                }

                is BookAppointmentEvent.OnSelectedAppointment -> {
                    _state.value = _state.value.copy(selectedAppointment = event.appointment)
                    _events.send(BookAppointmentUIEvent.ExpandSheet)
                }

                is BookAppointmentEvent.GetWorkingDates -> {
                    getWorkingDates()
                }

                is BookAppointmentEvent.Book -> {
                    book()
                }

                is BookAppointmentEvent.GetWorkers -> {
                    getWorkers()
                }

                is BookAppointmentEvent.GetAvailableAppointments -> {
                    getAvailableAppointments()
                }

                is BookAppointmentEvent.GetServices -> {
                    getServices()
                }

                is BookAppointmentEvent.OnRemoveHeadFromQueue -> {
                    removeHeadMessage()
                }
            }
        }
    }

    private suspend fun getWorkingDates() {
        val workerId = _state.value.selectedWorker
        val fromDate = getDateAsString()
        Log.d(TAG, "getWorkingDates: ${fromDate}")

        if (workerId == null)
            return

        loading(true)

        val result = workerRepository.getWorkingDates(workerId = workerId.id, fromDate = fromDate)

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(workingDates = result.value)
                Log.d(TAG, "getWorkingDates: ${result.value}")
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "GenericError: ${result.errorMessage}")
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = result.errorMessage
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
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
                Log.d(TAG, "GenericError: ${result.errorMessage}")
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = result.errorMessage
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
            }
        }


        loading(false)
    }


    private suspend fun getAvailableAppointments() {
        val workerId = _state.value.selectedWorker?.id
        val workingDate = _state.value.selectedWorkingDate

        if (workerId == null || workingDate == null)
            return

        loading(true)

        val result = repository.getAvailableAppointments(
            workingDate.id,
            workerId
        )

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(availableAppointments = result.value)
                Log.d(TAG, "getAvailableAppointments: ${result.value}")
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "GenericError: ${result.errorMessage}")
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = result.errorMessage
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
            }
        }


        loading(false)
    }


    private suspend fun getServices() {
        val workerId = _state.value.selectedWorker?.id ?: return

        loading(true)
        val list = listOf(
            ServiceType.HairCut("Hair Cut", application.getString(R.string.hair_cut)),
            ServiceType.HairCut("Wax", application.getString(R.string.wax)),
        )

        _state.value = _state.value.copy(services = list)

        loading(false)
    }


    private suspend fun book() {
        val worker = _state.value.selectedWorker
        val service = _state.value.selectedService
        val appointment = _state.value.selectedAppointment

        if (appointment == null || worker == null || service.isBlank())
            return

        loading(true)

        val appointmentDTO =
            BookAppointmentDto(
                workerId = worker.id,
                service = service,
                appointmentId = appointment.id
            )

        val result = repository.bookAppointment(appointmentDTO)

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(
                    selectedWorker = null,
                    selectedWorkingDate = null,
                    selectedService = "",
                    selectedAppointment = null,
                    workingDates = listOf(),
                    services = listOf(),
                    availableAppointments = listOf(),
                )
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Booked",
                        description = "the appointment has been booked"
                    )
                )
                _events.send(BookAppointmentUIEvent.OnBookAppointment(result.value))
            }
            is ApiResult.GenericError -> {
                Log.d(TAG, "GenericError: ${result.errorMessage}")
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = result.errorMessage
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
            }
        }

        loading(false)
    }


}