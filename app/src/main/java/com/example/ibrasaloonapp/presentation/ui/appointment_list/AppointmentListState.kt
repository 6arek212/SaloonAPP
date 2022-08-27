package com.example.ibrasaloonapp.presentation.ui.appointment_list

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.core.domain.UIComponent

data class AppointmentListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val activeAppointments: List<Appointment> = listOf(),
    val historyAppointments: List<Appointment> = listOf(),
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())
) {
}