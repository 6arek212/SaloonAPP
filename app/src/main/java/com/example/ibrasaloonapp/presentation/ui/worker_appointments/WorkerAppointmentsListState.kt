package com.example.ibrasaloonapp.presentation.ui.worker_appointments

import com.example.ibrasaloonapp.domain.model.Appointment

data class WorkerAppointmentsListState(
    val appointments: List<Appointment> = listOf(),
    val selectedDate: DayCardData? = null,
    val dateRange: DateRange? = null,
    val search: String = "",
    val isRefreshing: Boolean = false,
    val filter: AppointmentFilter = AppointmentFilter.ShowAll,
    val dates: List<DayCardData> = listOf(),
    val updateStatusDialogVisibility: Boolean = false
)


data class DateRange(val from: String, val to: String, val monthString: String)
