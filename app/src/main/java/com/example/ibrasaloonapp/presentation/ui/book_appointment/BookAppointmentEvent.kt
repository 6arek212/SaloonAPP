package com.example.ibrasaloonapp.presentation.ui.book_appointment

sealed class BookAppointmentEvent {
    data class DateChanged(val date: String) : BookAppointmentEvent()
    data class ServiceTypeChanged(val type: String) : BookAppointmentEvent()
    data class TimeChanged(val time: String) : BookAppointmentEvent()

    data class ServiceTypeDropDownExpandChange(val value: Boolean) : BookAppointmentEvent()
    data class TimeDropDownExpandChange(val value: Boolean) : BookAppointmentEvent()

    object Submit : BookAppointmentEvent()
    object GetServicesTypes : BookAppointmentEvent()
    object GetAvailableAppointments : BookAppointmentEvent()
    object OnRemoveHeadFromQueue : BookAppointmentEvent()

}