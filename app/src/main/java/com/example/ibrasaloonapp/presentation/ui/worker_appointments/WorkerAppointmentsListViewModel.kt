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
import com.example.ibrasaloonapp.use.CreateAppointmentsUseCase
import com.example.ibrasaloonapp.use.DeleteAppointmentsUseCase
import com.example.ibrasaloonapp.use.GetWorkerAppointmentsUseCase
import com.example.ibrasaloonapp.use.UpdateAppointmentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
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
    val createAppointmentsUseCase: CreateAppointmentsUseCase,
    val deleteAppointmentsUseCase: DeleteAppointmentsUseCase
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
                is WorkerAppointmentsListEvent.ShowStatusDialog -> {
                    _state.value = _state.value.copy(updateStatusDialogVisibility = true)
                    appointmentToUpdateId = event.id
                    appointmentToUpdateIndex = event.index
                }

                is WorkerAppointmentsListEvent.DismissStatusDialog -> {
                    _state.value = _state.value.copy(updateStatusDialogVisibility = false)
                    appointmentToUpdateId = null
                    appointmentToUpdateIndex = null
                }

                is WorkerAppointmentsListEvent.UpdateAppointmentStatus -> {
                    updateAppointmentStatus(event.status)
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

    private suspend fun deleteAppointment() {
        val id = appointmentToUpdateId ?: return
        val index = appointmentToUpdateIndex ?: return

        _state.value = _state.value.copy(updateStatusDialogVisibility = false)
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
                        UIComponent.Snackbar(message = "Appointment Deleted")
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
                UIComponent.Snackbar(message = "Make sure you picked a start and end time")
            )
        }


        val workerId = authRepository.getUserId() ?: return
        val selectedDate = _state.value.selectedDate ?: return
        val fullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
        var startDate = ZonedDateTime.parse(selectedDate.startDate, fullDate)
        startDate = startDate.withHour(startHour.toInt())
        startDate = startDate.withMinute(startMin.toInt())
        startDate = startDate.withSecond(0)

        var endDate = ZonedDateTime.parse(selectedDate.startDate, fullDate)
        endDate = endDate.withHour(endHour.toInt())
        endDate = endDate.withMinute(endMin.toInt())
        endDate = endDate.withSecond(0)

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
                        UIComponent.Snackbar(message = "Appointment Created")
                    )
                }

                is Resource.Error -> {
                    sendMessage(UIComponent.Snackbar(message = result.message))
                }
            }
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


    private suspend fun updateAppointmentStatus(status: String) {
        val id = appointmentToUpdateId ?: return
        val index = appointmentToUpdateIndex ?: return

        _state.value = _state.value.copy(updateStatusDialogVisibility = false)
        updateAppointmentStatusUseCase(appointmentId = id, status = status).collect { result ->
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


}