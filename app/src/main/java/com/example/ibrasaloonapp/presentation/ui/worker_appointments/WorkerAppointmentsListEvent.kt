package com.example.ibrasaloonapp.presentation.ui.worker_appointments

sealed class WorkerAppointmentsListEvent {

    object Search : WorkerAppointmentsListEvent()
    class OnSearchChange(val search: String) : WorkerAppointmentsListEvent()
    class OnSelectedDate(val date: DayCardData) : WorkerAppointmentsListEvent()
    object GetAppointments : WorkerAppointmentsListEvent()
    object CurrentWeekRange : WorkerAppointmentsListEvent()
    object DecreaseWeekRange : WorkerAppointmentsListEvent()
    object IncreaseWeekRange : WorkerAppointmentsListEvent()
    object DismissStatusDialog : WorkerAppointmentsListEvent()
    class UpdateAppointmentStatus(val status: String) : WorkerAppointmentsListEvent()
    class ShowStatusDialog(val id: String, val index: Int) : WorkerAppointmentsListEvent()
    object Refresh : WorkerAppointmentsListEvent()
    class CreateAppointment(
        val startHour: String,
        val startMin: String,
        val endHour: String,
        val endMin: String,
        val status: String? = null
    ) :
        WorkerAppointmentsListEvent()

    object DismissUIMessage : WorkerAppointmentsListEvent()
    object DeleteAppointment : WorkerAppointmentsListEvent()


    class ChangeFilter(val filter: AppointmentFilter) : WorkerAppointmentsListEvent()

}


enum class AppointmentFilter(val value: String?) {
    InProgress("in-progress"),
    ShowAll(null)
}