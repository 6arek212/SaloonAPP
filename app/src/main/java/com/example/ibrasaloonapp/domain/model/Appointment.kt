package com.example.ibrasaloonapp.domain.model

import android.os.Parcelable
import com.example.ibrasaloonapp.core.KeyValueWrapper
import kotlinx.parcelize.Parcelize


data class Appointment(
    val id: String,
    val service: Service? = null,
    val startTime: String,
    val endTime: String,
    val status: String,
    val customer: User? = null,
    val worker: User
)