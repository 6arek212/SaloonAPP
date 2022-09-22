package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.User
import com.google.gson.annotations.SerializedName

data class AppointmentDto(

    @SerializedName("_id")
    val id: String,

    @SerializedName("customer")
    val customer: UserDto? = null,

    @SerializedName("worker")
    val worker: UserDto,

    @SerializedName("service")
    val service: ServiceDto? = null,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("status")
    val status: String
)
