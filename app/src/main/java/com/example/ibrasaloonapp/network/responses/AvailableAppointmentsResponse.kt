package com.example.ibrasaloonapp.network.responses

data class AvailableAppointmentsResponse(
    val message: String,
    val times: List<String>
) {
}