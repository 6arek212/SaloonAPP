package com.example.ibrasaloonapp.presentation.ui.home

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.presentation.ui.edit_profile.EditProfileEvent

sealed class HomeEvent {
    object GetAppointment : HomeEvent()
    object GetWorkers : HomeEvent()
    object Refresh : HomeEvent()
    object DismissLoginDialog : HomeEvent()
    object ShowLoginDialog : HomeEvent()
    class UpdateAppointment(val appointment: Appointment) : HomeEvent()
    object OnRemoveHeadFromQueue : HomeEvent()

}