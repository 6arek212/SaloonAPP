package com.example.ibrasaloonapp.presentation.ui.book_appointment

import com.example.ibrasaloonapp.core.getCurrentDateAsString
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.domain.model.WorkingDate
import java.util.*

data class BookAppointmentState(
    val workers: List<User> = listOf(),
    val workingDates: List<WorkingDate> = listOf(),
    val services: List<ServiceType> = listOf(),
    val availableAppointments: List<Appointment> = listOf(),

    val selectedWorker: User? = null,
    val selectedWorkingDate: WorkingDate? = null,
    val selectedService: String = "",
    val selectedAppointment: Appointment? = null,

    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())

) {
}

sealed class ServiceType(val v: String, val t: String) {
    class HairCut(val name: String, val value: String) : ServiceType(v = name, t = value)
}
