package com.example.ibrasaloonapp.domain.model


data class Appointment(
    val id: String,
    val type: String? = null,
    val date: String? = null,
    val time: String? = null,
    val isActive: Boolean? = null,
    val createdAt: String? = null,
    val customer: String? = null
) {
}