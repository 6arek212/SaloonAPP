package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class ServiceDto(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
)