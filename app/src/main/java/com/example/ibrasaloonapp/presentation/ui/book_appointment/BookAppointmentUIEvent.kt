package com.example.ibrasaloonapp.presentation.ui.book_appointment

sealed class BookAppointmentUIEvent {

    object ExpandSheet : BookAppointmentUIEvent()
    object HideSheet : BookAppointmentUIEvent()
    object OnBookAppointment : BookAppointmentUIEvent()
}