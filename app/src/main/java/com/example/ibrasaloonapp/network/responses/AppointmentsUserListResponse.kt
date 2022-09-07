package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.AppointmentDto

data class AppointmentsUserListResponse(
    val message: String,
    val appointments: List<AppointmentDto>
) {
}