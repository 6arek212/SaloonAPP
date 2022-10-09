package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class AddServiceDto(
    @SerializedName("worker")
    val workerId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Float,
)