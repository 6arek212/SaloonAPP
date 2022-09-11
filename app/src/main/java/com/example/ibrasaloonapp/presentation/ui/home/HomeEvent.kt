package com.example.ibrasaloonapp.presentation.ui.home

import com.example.ibrasaloonapp.domain.model.Appointment

sealed class HomeEvent {
    object GetAppointment : HomeEvent()
    object GetWorkers : HomeEvent()
    object Refresh : HomeEvent()
    class UpdateAppointment(val appointment: Appointment) : HomeEvent()
}