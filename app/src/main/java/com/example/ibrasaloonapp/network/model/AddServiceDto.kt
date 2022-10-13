package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class AddServiceDto(
    @SerializedName("workerId")
    val workerId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
)