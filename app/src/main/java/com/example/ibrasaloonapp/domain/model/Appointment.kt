package com.example.ibrasaloonapp.domain.model


data class Appointment(
    val id: String,
    val service: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val isActive: Boolean? = null,
    val customer: String? = null
) {
}