package com.example.ibrasaloonapp.presentation.ui.appointment_list

sealed class AppointmentListEvent {
    object GetAppointments : AppointmentListEvent()
    object Refresh : AppointmentListEvent()
    class UnBookAppointment(val id: String, val index: Int) : AppointmentListEvent()
    class ShowUnbookConfirmDialog(val id: String, val index: Int) : AppointmentListEvent()
    object DismissDialog : AppointmentListEvent()
}