package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.AppointmentDto

data class AppointmentListResponse(
    val message: String,
    val count: Int,
    val activeAppointments: List<AppointmentDto>,
    val historyAppointments: List<AppointmentDto>
) {
}