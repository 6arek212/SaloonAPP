package com.example.ibrasaloonapp.presentation.ui.home

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.presentation.ui.edit_profile.EditProfileEvent

sealed class HomeEvent {
    object GetAppointment : HomeEvent()
    object GetWorkers : HomeEvent()
    class GetData(val isAuthed: Boolean) : HomeEvent()
    class Refresh(val isAuthed: Boolean) : HomeEvent()
    object OnRemoveHeadFromQueue : HomeEvent()
    object Rest : HomeEvent()

}