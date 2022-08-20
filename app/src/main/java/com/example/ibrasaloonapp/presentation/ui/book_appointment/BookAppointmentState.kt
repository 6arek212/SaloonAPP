package com.example.ibrasaloonapp.presentation.ui.book_appointment

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.ProgressBarState

data class BookAppointmentState(
    val date: String = "",
    val dateError: String? = null,
    val serviceType: String = "",
    val serviceTypeError: String? = null,
    val time: String = "",
    val timeError: String? = null,

    val appointmentsList: List<String> = listOf(),
    val typesList: List<String> = listOf(),

    val expandDropDown1: Boolean = false,
    val expandDropDown2: Boolean = false,

    val progressBarState: ProgressBarState = ProgressBarState.Idle,

    ) {
}