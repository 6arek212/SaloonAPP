package com.example.ibrasaloonapp.domain.model

import android.os.Parcelable
import com.example.ibrasaloonapp.presentation.ui.book_appointment.ServiceType
import kotlinx.parcelize.Parcelize


@Parcelize
data class Appointment(
    val id: String,
    val service: ServiceType? = null,
    val startTime: String,
    val endTime: String,
    val isActive: Boolean,
    val customer: User? = null,
    val worker: User
) : Parcelable {
}