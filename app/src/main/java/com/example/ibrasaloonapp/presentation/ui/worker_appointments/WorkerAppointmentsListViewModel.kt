package com.example.ibrasaloonapp.presentation.ui.worker_appointments

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.CreateAppointmentDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.use.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


data class DayCardData(
    val startDate: String,
    val endDate: String,
    val textDate: String,
    val numDay: String
)

private const val TAG = "WorkerAppointmentsListV"


@HiltViewModel
class WorkerAppointmentsListViewModel
@Inject
constructor(
    val context: Application,
    val authRepository: AuthRepository,
    val getWorkerAppointmentsUseCase: GetWorkerAppointmentsUseCase,
    val updateAppointmentStatusUseCase: UpdateAppointmentStatus,
    val createAppointmentsUseCase: CreateAppointmentUseCase,
    val deleteAppointmentsUseCase: DeleteAppointmentsUseCase,
    val getWorkerServicesUseCase: GetWorkerServicesUseCase,
    val addServiceUseCase: AddServiceUseCase,
    val deleteServiceUseCase: DeleteServiceUseCase,
    val createRangeAppointmentsUseCase: CreateRangeAppointmentsUseCase
) : BaseViewModel() {

    private val _state: MutableState<WorkerAppointmentsListState> =
        mutableStateOf(WorkerAppointmentsListState())
    val state: State<WorkerAppointmentsListState> = _state


    private var startDate: LocalDate = LocalDate.now()
    private var appointmentToUpdateId: String? = null
    private var appointmentToUpdateIndex: Int? = null

    init {
        getDates(true)
    }

    fun onTriggerEvent(event: WorkerAppointmentsListEvent) {
        viewModelScope.launch {

            when (event) {
                is WorkerAppointmentsListEvent.GetServices -> {
                    getServices()
                }
                is WorkerAppointmentsListEvent.DeleteService -> {
                    deleteService(serviceId = event.serviceId, index = event.index)
                }

                is WorkerAppointmentsListEvent.AddService -> {
                    addService(title = event.title, priceString = event.price)
                }

                is WorkerAppointmentsListEvent.UpdateAppointmentId -> {
                    appointmentToUpdateId = event.id
                    appointmentToUpdateIndex = event.index
                    _state.value = _state.value.copy(appointmentForUpdate = _state.value.appointments[event.index])
                }

                is WorkerAppointmentsListEvent.UpdateAppointmentStatus -> {
                    updateAppointmentStatus(event.status, event.service)
                }

                is WorkerAppointmentsListEvent.DismissUIMessage -> {
                    removeMessage()
                }

                is WorkerAppointmentsListEvent.CurrentWeekRange -> {
                    currentWeekRange()
                }

                is WorkerAppointmentsListEvent.IncreaseWeekRange -> {
                    increaseWeek()
                }

                is WorkerAppointmentsListEvent.DecreaseWeekRange -> {
                    decreaseWeek()
                }

                is WorkerAppointmentsListEvent.GetAppointments -> {
                    getWorkerAppointments()
                }

                is WorkerAppointmentsListEvent.DeleteAppointment -> {
                    deleteAppointment()
                }

                is WorkerAppointmentsListEvent.CreateAppointment -> {
                    createAppointment(
                        startHour = event.startHour,
                        startMin = event.startMin,
                        endHour = event.endHour,
                        endMin = event.endMin,
                        status = event.status
                    )
                }

                is WorkerAppointmentsListEvent.CreateRangeAppointments -> {
                    createRangeAppointments(
                        startHour = event.startHour,
                        startMin = event.startMin,
                        endHour = event.endHour,
                        endMin = event.endMin,
                        status = event.status,
                        interval = event.interval
                    )
                }

                is WorkerAppointmentsListEvent.OnSelectedDate -> {
                    _state.value = _state.value.copy(selectedDate = event.date)
                    getWorkerAppointments()
                }

                is WorkerAppointmentsListEvent.Search -> {
                    getWorkerAppointments()
                }

                is WorkerAppointmentsListEvent.ChangeFilter -> {
                    _state.value = _state.value.copy(filter = event.filter)
                    getWorkerAppointments()
                }

                is WorkerAppointmentsListEvent.OnSearchChange -> {
                    _state.value = _state.value.copy(search = event.search)
                }

                is WorkerAppointmentsListEvent.Refresh -> {
                    _state.value = _state.value.copy(isRefreshing = true)
                    getWorkerAppointments()
                    _state.value = _state.value.copy(isRefreshing = false)
                }
            }
        }
    }

    private suspend fun createRangeAppointments(
        startHour: String,
        startMin: String,
        endHour: String,
        endMin: String,
        status: String?,
        interval: String
    ) {
        if (startHour.isBlank() || startMin.isBlank() || endHour.isBlank() || endMin.isBlank()) {
            return sendMessage(
                UIComponent.Snackbar(message = context.getString(R.string.make_sure_picked_start_end_time))
            )
        }
        val workerId = authRepository.getUserId() ?: return
        val selectedDate = _state.value.selectedDate ?: return
        val fullDate =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
        var startDate = ZonedDateTime.parse(selectedDate.startDate, fullDate)
        startDate = startDate.withHour(startHour.toInt())
        startDate = startDate.withMinute(startMin.toInt())
        startDate = startDate.withSecond(0)

        var endDate = ZonedDateTime.parse(selectedDate.startDate, fullDate)
        endDate = endDate.withHour(endHour.toInt())
        endDate = endDate.withMinute(endMin.toInt())
        endDate = endDate.withSecond(0)

        if (endDate.isBefore(startDate)) {
            return sendMessage(UIComponent.Snackbar(message = context.getString(R.string.end_time_bigger_than_start_time)))
        }


        val startDateTime = startDate.format(fullDate)
        val endDateTime = endDate.format(fullDate)

        Log.d(TAG, "createAppointment: $startDate    $endDate ")

        try {

            val data = CreateAppointmentDto(
                workerId = workerId,
                startTime = startDateTime,
                endTime = endDateTime,
                status = status,
                interval = interval.toInt()
            )

            createRangeAppointmentsUseCase(appointmentData = data).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        loading(result.value)
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "getWorkerAppointments: ${result.data}")
                        onTriggerEvent(WorkerAppointmentsListEvent.GetAppointments)
                        sendMessage(
                            UIComponent.Snackbar(message = context.getString(R.string.appointment_created))
                        )
                    }

                    is Resource.Error -> {
                        sendMessage(UIComponent.Snackbar(message = result.message))
                    }
                }
            }
        } catch (e: Exception) {

        }
    }

    private suspend fun deleteAppointment() {
        val id = appointmentToUpdateId ?: return
        val index = appointmentToUpdateIndex ?: return

        deleteAppointmentsUseCase(appointmentId = id).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    loading(result.value)
                }

                is Resource.Success -> {
                    Log.d(TAG, "getWorkerAppointments: ${result.data}")
                    val list = ArrayList(_state.value.appointments)
                    list.removeAt(index)
                    _state.value = _state.value.copy(appointments = list)
                    sendMessage(
                        UIComponent.Snackbar(message = context.getString(R.string.appointment_deleted))
                    )
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Snackbar(message = result.message)
                    )
                }
            }
        }
    }

    private suspend fun createAppointment(
        startHour: String,
        startMin: String,
        endHour: String,
        endMin: String,
        status: String? = null
    ) {
        if (startHour.isBlank() || startMin.isBlank() || endHour.isBlank() || endMin.isBlank()) {
            return sendMessage(
                UIComponent.Snackbar(message = context.getString(R.string.make_sure_picked_start_end_time))
            )
        }


        try {
            val workerId = authRepository.getUserId() ?: return
            val selectedDate = _state.value.selectedDate ?: return
            val fullDate =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
            var startDate = ZonedDateTime.parse(selectedDate.startDate, fullDate)
            startDate = startDate.withHour(startHour.toInt())
            startDate = startDate.withMinute(startMin.toInt())
            startDate = startDate.withSecond(0)

            var endDate = ZonedDateTime.parse(selectedDate.startDate, fullDate)
            endDate = endDate.withHour(endHour.toInt())
            endDate = endDate.withMinute(endMin.toInt())
            endDate = endDate.withSecond(0)

            if (endDate.isBefore(startDate)) {
                return sendMessage(UIComponent.Snackbar(message = context.getString(R.string.end_time_bigger_than_start_time)))
            }

            val startDateTime = startDate.format(fullDate)
            val endDateTime = endDate.format(fullDate)

            Log.d(TAG, "createAppointment: $startDate    $endDate ")

            val data = CreateAppointmentDto(
                workerId = workerId,
                startTime = startDateTime,
                endTime = endDateTime,
                status = status
            )

            createAppointmentsUseCase(appointmentData = data).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        loading(result.value)
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "getWorkerAppointments: ${result.data}")
                        onTriggerEvent(WorkerAppointmentsListEvent.GetAppointments)
                        sendMessage(
                            UIComponent.Snackbar(message = context.getString(R.string.appointment_created))
                        )
                    }

                    is Resource.Error -> {
                        sendMessage(UIComponent.Snackbar(message = result.message))
                    }
                }
            }
        } catch (e: Exception) {

        }
    }


    private fun currentWeekRange() {
        startDate = LocalDate.now()
        getDates()
    }


    private fun increaseWeek() {
        startDate = startDate.plusDays(5)
        getDates()
    }

    private fun decreaseWeek() {
        startDate = startDate.minusDays(5)
        getDates()
    }


    private fun getDates(updateSelectedDate: Boolean = false) {
        val list = ArrayList<DayCardData>()
        val dayString = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
        val dayNumber = DateTimeFormatter.ofPattern("dd", Locale.getDefault())
        val monthString = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
        val fullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())


        for (i in 0..4) {
            val date1 = startDate.plusDays(i.toLong())
            val date2 = startDate.plusDays(i.toLong() + 1)

            val textDate: String = date1.format(dayString)
            val numDay: String = date1.format(dayNumber)

            val startDate = ZonedDateTime.of(
                date1,
                LocalTime.MIN,
                ZoneId.systemDefault()
            ).format(fullDate)

            val endDate = ZonedDateTime.of(
                date2,
                LocalTime.MIN,
                ZoneId.systemDefault()
            ).format(fullDate)


            list.add(
                DayCardData(
                    textDate = textDate,
                    numDay = numDay,
                    startDate = startDate,
                    endDate = endDate
                )
            )
        }

        _state.value = _state.value.copy(
            dates = list,
            dateRange = DateRange(
                from = list.first().numDay,
                to = list.last().numDay,
                monthString = startDate.format(monthString)
            )
        )

        if (updateSelectedDate) {
            _state.value = _state.value.copy(selectedDate = list.first())
        }
    }


    private suspend fun updateAppointmentStatus(status: String, service: String? = null) {
        val id = appointmentToUpdateId ?: return
        val index = appointmentToUpdateIndex ?: return

        updateAppointmentStatusUseCase(
            appointmentId = id,
            status = status,
            service = service
        ).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    loading(result.value)
                }

                is Resource.Success -> {
                    Log.d(TAG, "getWorkerAppointments: ${result.data}")
                    result.data?.let {
                        val list = _state.value.appointments
                        val newList = ArrayList(list)
                        newList[index] = it
                        _state.value = _state.value.copy(
                            appointments = newList
                        )
                        appointmentToUpdateId = null
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Snackbar(message = result.message)
                    )
                }
            }
        }

    }


    private suspend fun getWorkerAppointments() {
        val startDate = _state.value.selectedDate?.startDate ?: return
        val endDate = _state.value.selectedDate?.endDate ?: return
        val filter = _state.value.filter.value
        Log.d(TAG, "getWorkerAppointments: ${authRepository.getUserId()}")

        val userId = authRepository.getUserId() ?: return
        val search = _state.value.search

        getWorkerAppointmentsUseCase(
            startDate = startDate,
            endDate = endDate,
            search = search,
            status = filter,
            userId = userId
        ).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    loading(result.value)
                }

                is Resource.Success -> {
                    Log.d(TAG, "getWorkerAppointments: ${result.data}")
                    result.data?.let {
                        _state.value = _state.value.copy(appointments = result.data)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Snackbar(message = result.message)
                    )
                }
            }
        }
    }


    private suspend fun getServices() {
        val userId = authRepository.getUserId() ?: return
        getWorkerServicesUseCase(workerId = userId).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    loading(result.value)
                }

                is Resource.Success -> {
                    Log.d(TAG, "getServices: ${result.data}")
                    result.data?.let {
                        _state.value = _state.value.copy(services = it)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Snackbar(message = result.message)
                    )
                }
            }
        }
    }


    private suspend fun deleteService(serviceId: String, index: Int) {

        deleteServiceUseCase(serviceId = serviceId).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    loading(result.value)
                }

                is Resource.Success -> {
                    Log.d(TAG, "deleteService: ${result.data}")
                    result.data?.let {
                        val list = _state.value.services
                        val newList = ArrayList(list)
                        newList.removeAt(index)
                        _state.value = _state.value.copy(services = newList)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Snackbar(message = result.message)
                    )
                }
            }
        }

    }


    private suspend fun addService(title: String, priceString: String) {

        try {
            val userId = authRepository.getUserId() ?: return
            if (title.isBlank()) {
                return sendMessage(
                    UIComponent.Snackbar(message = context.getString(R.string.must_pick_service))
                )
            }
            val price = priceString.toDouble()



            addServiceUseCase(
                workerId = userId,
                title = title,
                price = price
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        loading(result.value)
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "addService: ${result.data}")
                        result.data?.let {
                            val list = _state.value.services
                            val newList = ArrayList(list)
                            newList.add(it)
                            _state.value = _state.value.copy(services = newList)
                        }
                    }

                    is Resource.Error -> {
                        sendMessage(
                            UIComponent.Snackbar(message = result.message)
                        )
                    }
                }
            }
        } catch (e: NumberFormatException) {
            sendMessage(
                UIComponent.Snackbar(message = context.getString(R.string.price_must_not_be_empty))
            )
        }

    }

}