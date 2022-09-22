package com.example.ibrasaloonapp.presentation.ui.book_appointment

import android.os.Parcelable
import com.example.ibrasaloonapp.core.KeyValueWrapper
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.Service
import com.example.ibrasaloonapp.domain.model.User
import kotlinx.parcelize.Parcelize

data class BookAppointmentState(
    val workers: List<User> = listOf(),
    val workingDates: List<String> = listOf(),
    val services: List<Service> = listOf(),
    val availableAppointments: List<Appointment> = listOf(),

    val selectedWorker: User? = null,
    val selectedWorkingDate: String? = null,
    val selectedService: Service? = null,
    val selectedAppointment: Appointment? = null
) {
}

//@Parcelize
//sealed class ServiceType(val key: String, val value: String) : Parcelable {
//    class HairCut(val n: String, val v: String) : ServiceType(key = n, value = v)
//}
//
