package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.User
import com.google.gson.annotations.SerializedName

data class AppointmentDto(

    @SerializedName("_id")
    val id: String,

    @SerializedName("customer")
    val customer: String? = null,

    @SerializedName("worker")
    val worker: User,

    @SerializedName("service")
    val service: String? = null,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("isActive")
    val isActive: Boolean? = null
)
