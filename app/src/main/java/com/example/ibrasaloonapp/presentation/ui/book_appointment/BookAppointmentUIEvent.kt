package com.example.ibrasaloonapp.presentation.ui.book_appointment

import com.example.ibrasaloonapp.domain.model.Appointment

sealed class BookAppointmentUIEvent {

    object ExpandSheet : BookAppointmentUIEvent()
    object HideSheet : BookAppointmentUIEvent()
    class OnBookAppointment(val appointment: Appointment) : BookAppointmentUIEvent()
}