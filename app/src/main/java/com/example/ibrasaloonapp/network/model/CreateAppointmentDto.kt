package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class CreateAppointmentDto(
    @SerializedName("worker")
    val workerId: String,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("interval")
    val interval: Int? = null,
) {
}