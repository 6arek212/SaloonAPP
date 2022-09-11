package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.AppointmentDto

data class AppointmentNotNulResponse(
    val appointment: AppointmentDto,
    val message: String
) {
}