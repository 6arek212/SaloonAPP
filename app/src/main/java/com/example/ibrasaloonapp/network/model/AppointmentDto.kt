package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class AppointmentDto(

    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("customer")
    val customer: CustomerDto? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("time")
    val time: String? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null
) {
}