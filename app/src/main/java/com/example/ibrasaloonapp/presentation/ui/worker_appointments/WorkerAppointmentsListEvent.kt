package com.example.ibrasaloonapp.presentation.ui.worker_appointments


sealed class WorkerAppointmentsListEvent {

    class AddService(val title: String, val price: String) : WorkerAppointmentsListEvent()
    class DeleteService(val serviceId: String, val index: Int) : WorkerAppointmentsListEvent()
    object GetServices : WorkerAppointmentsListEvent()


    object Search : WorkerAppointmentsListEvent()
    class OnSearchChange(val search: String) : WorkerAppointmentsListEvent()
    class OnSelectedDate(val date: DayCardData) : WorkerAppointmentsListEvent()
    object GetAppointments : WorkerAppointmentsListEvent()
    object CurrentWeekRange : WorkerAppointmentsListEvent()
    object DecreaseWeekRange : WorkerAppointmentsListEvent()
    object IncreaseWeekRange : WorkerAppointmentsListEvent()
    class UpdateAppointmentStatus(val status: String, val service: String? = null) :
        WorkerAppointmentsListEvent()

    class UpdateAppointmentId(val id: String, val index: Int) : WorkerAppointmentsListEvent()
    object Refresh : WorkerAppointmentsListEvent()
    class CreateAppointment(
        val startHour: String,
        val startMin: String,
        val endHour: String,
        val endMin: String,
        val status: String? = null
    ) :
        WorkerAppointmentsListEvent()

    class CreateRangeAppointments(
        val startHour: String,
        val startMin: String,
        val endHour: String,
        val endMin: String,
        val interval: String,
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