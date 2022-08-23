package com.example.ibrasaloonapp.presentation.ui.book_appointment

import com.example.ibrasaloonapp.core.getCurrentDateAsString
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import java.util.*

data class BookAppointmentState(
    val date: String = getCurrentDateAsString(),
    val dateError: String? = null,
    val serviceType: String = "",
    val serviceTypeError: String? = null,
    val time: String = "",
    val timeError: String? = null,

    val availableAppointmentsTimesList: List<String> = listOf(),
    val typesList: List<String> = listOf(),

    val expandDropDown1: Boolean = false,
    val expandDropDown2: Boolean = false,

    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())

    ) {
}