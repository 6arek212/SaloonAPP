package com.example.ibrasaloonapp.presentation.ui.appointment_list

sealed class AppointmentListEvent {
    object GetSessions : AppointmentListEvent()
    object OnRemoveHeadFromQueue: AppointmentListEvent()
}