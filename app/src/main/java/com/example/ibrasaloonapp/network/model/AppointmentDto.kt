package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class AppointmentDto(

    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("customer")
    val customer: String? = null,

    @SerializedName("service")
    val service: String? = null,

    @SerializedName("start_time")
    val startTime: String? = null,

    @SerializedName("end_time")
    val endTime: String? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null
    )
