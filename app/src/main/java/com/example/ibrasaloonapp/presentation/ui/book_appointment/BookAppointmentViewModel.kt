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
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.use.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BookAppointmentViewMode"

@HiltViewModel
class BookAppointmentViewModel
@Inject
constructor(
    private val context: Application,
    private val authRepository: AuthRepository,
    private val getWorkersUseCase: GetWorkersUseCase,
    private val bookAppointmentUseCase: BookAppointmentUseCase,
    private val getAvailableAppointmentUseCase: GetAvailableAppointmentUseCase,
    private val getWorkerServicesUseCase: GetWorkerServicesUseCase,
    private val getWorkingDatesUseCase: GetWorkingDatesUseCase,
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
                        selectedService = null,
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
                        selectedService = null,
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
        val workerId = _state.value.selectedWorker?.id ?: return
        val fromDate = getDateAsString()
        Log.d(TAG, "getWorkingDates: $fromDate $workerId")

        getWorkingDatesUseCase(fromDate = fromDate, workerId = workerId).onEach {
            when (it) {

                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { workingDates ->
                        _state.value = _state.value.copy(workingDates = workingDates)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = it.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    private suspend fun getWorkers() {
        getWorkersUseCase().onEach {
            when (it) {

                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { workers ->
                        _state.value = _state.value.copy(workers = workers)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = it.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    private suspend fun getAvailableAppointments() {
        val workerId = _state.value.selectedWorker?.id
        val workingDate = _state.value.selectedWorkingDate

        if (workerId == null || workingDate == null)
            return


        getAvailableAppointmentUseCase(
            workingDate = workingDate,
            workerId = workerId
        ).onEach { data ->
            when (data) {

                is Resource.Loading -> {
                    loading(data.value)
                }

                is Resource.Success -> {
                    data.data?.let { availableAppointments ->
                        _state.value =
                            _state.value.copy(availableAppointments = availableAppointments)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = data.message
                        )
                    )
                }
            }

        }.launchIn(viewModelScope)

    }


    private suspend fun getServices() {
        val workerId = _state.value.selectedWorker?.id ?: return
        getWorkerServicesUseCase(workerId = workerId).onEach { data ->
            when (data) {
                is Resource.Loading -> {
                    loading(data.value)
                }

                is Resource.Success -> {
                    data.data?.let { services ->
                        _state.value = _state.value.copy(services = services)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = data.message
                        )
                    )
                }
            }

        }.launchIn(viewModelScope)
    }


    private suspend fun book() {
        val service = _state.value.selectedService?.id ?: return
        val appointment = _state.value.selectedAppointment ?: return
        val userId = authRepository.getUserId() ?: return


        bookAppointmentUseCase(
            service = service,
            appointmentId = appointment.id,
            userId = userId
        ).onEach {
            when (it) {

                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { appointment ->
                        _state.value = _state.value.copy(
                            selectedWorker = null,
                            selectedWorkingDate = null,
                            selectedService = null,
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
                        _events.send(BookAppointmentUIEvent.OnBookAppointment(appointment))
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = it.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)

    }


}