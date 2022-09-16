package com.example.ibrasaloonapp.presentation.ui.book_appointment

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.core.getDateAsString
import com.example.ibrasaloonapp.domain.use_case.ValidateRequired
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.BookAppointmentDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.repository.WorkerRepository
import com.example.ibrasaloonapp.ui.CustomString
import com.example.trainingapp.network.NetworkErrors
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
    private val userId: CustomString,
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
                    removeMessage()
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

                val message = when (result.code) {

                    NetworkErrors.ERROR_400 -> {
                        context.getString(R.string.bad_request)
                    }

                    NetworkErrors.ERROR_401 -> {
                        context.getString(R.string.not_authorized)
                    }

                    else -> {
                        context.getString(R.string.something_went_wrong)
                    }
                }

                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = message
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                sendMessage(
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

                val message = when (result.code) {

                    NetworkErrors.ERROR_400 -> {
                        context.getString(R.string.bad_request)
                    }

                    NetworkErrors.ERROR_401 -> {
                        context.getString(R.string.not_authorized)
                    }

                    else -> {
                        context.getString(R.string.something_went_wrong)
                    }
                }

                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = message
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                sendMessage(
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
            workingDate,
            workerId
        )

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(availableAppointments = result.value)
                Log.d(TAG, "getAvailableAppointments: ${result.value}")
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "GenericError: ${result.errorMessage}")

                val message = when (result.code) {

                    NetworkErrors.ERROR_400 -> {
                        context.getString(R.string.bad_request)
                    }

                    NetworkErrors.ERROR_401 -> {
                        context.getString(R.string.not_authorized)
                    }

                    else -> {
                        context.getString(R.string.something_went_wrong)
                    }
                }


                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = message
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                sendMessage(
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
        val service = _state.value.selectedService
        val appointment = _state.value.selectedAppointment
        val userId = userId.value ?: return

        if (appointment == null  || service.isBlank())
            return

        loading(true)

        val appointmentDTO =
            BookAppointmentDto(
                service = service,
                appointmentId = appointment.id,
                userId = userId
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
                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.booked),
                        description = context.getString(R.string.the_appointment_has_been_booked)
                    )
                )
                _events.send(BookAppointmentUIEvent.OnBookAppointment(result.value))
            }
            is ApiResult.GenericError -> {
                Log.d(TAG, "GenericError: ${result.errorMessage}")
                val message = when (result.code) {

                    NetworkErrors.ERROR_400 -> {
                        when (result.genericCode) {
                            1 -> context.getString(R.string.you_already_have_an_appointment)
                            2 -> context.getString(R.string.appointment_has_already_been_booked)
                            else -> context.getString(R.string.bad_request)
                        }
                    }

                    NetworkErrors.ERROR_401 -> {
                        context.getString(R.string.not_authorized)
                    }

                    else -> {
                        context.getString(R.string.something_went_wrong)
                    }
                }

                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = message
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                sendMessage(
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