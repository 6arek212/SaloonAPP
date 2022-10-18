package com.example.ibrasaloonapp.presentation.ui.appointment_list

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.core.domain.UIComponent

data class AppointmentListState(
    val appointments: List<Appointment> = listOf(),
    val activeAppointment: Appointment? = null,
    val isRefreshing: Boolean = false,
    val unBookDialog: UIComponent.Dialog? = null
)