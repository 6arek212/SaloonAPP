package com.example.ibrasaloonapp.presentation.ui.book_appointment

import android.os.Parcelable
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import kotlinx.parcelize.Parcelize
import java.util.*

data class BookAppointmentState(
    val workers: List<User> = listOf(),
    val workingDates: List<String> = listOf(),
    val services: List<ServiceType> = listOf(),
    val availableAppointments: List<Appointment> = listOf(),

    val selectedWorker: User? = null,
    val selectedWorkingDate: String? = null,
    val selectedService: String = "",
    val selectedAppointment: Appointment? = null
) {
}

@Parcelize
sealed class ServiceType(val name: String, val value: String) : Parcelable {
    class HairCut(val n: String, val v: String) : ServiceType(name = n, value = v)
}

