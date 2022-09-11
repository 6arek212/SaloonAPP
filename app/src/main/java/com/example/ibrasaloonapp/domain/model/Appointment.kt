package com.example.ibrasaloonapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Appointment(
    val id: String,
    val service: String? = null,
    val startTime: String,
    val endTime: String,
    val isActive: Boolean,
    val customer: String? = null,
    val worker: User
) : Parcelable {
}