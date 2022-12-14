package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.AppointmentDto

data class AppointmentResponse(
    val appointment: AppointmentDto? = null,
    val message: String
) {
}