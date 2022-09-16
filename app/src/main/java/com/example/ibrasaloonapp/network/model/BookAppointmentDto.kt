package com.example.ibrasaloonapp.network.model

data class BookAppointmentDto(
    val userId: String,
    val service: String,
    val appointmentId: String
) {
}