package com.example.ibrasaloonapp.presentation.ui.appointment_list

sealed class AppointmentListEvent {
    object GetAppointments : AppointmentListEvent()
    object OnRemoveHeadFromQueue : AppointmentListEvent()
    class UnBookAppointment(val id: String, val index: Int) : AppointmentListEvent()
}