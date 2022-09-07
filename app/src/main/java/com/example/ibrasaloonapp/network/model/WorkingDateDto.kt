package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class WorkingDateDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("date")
    val date: String,
    ) {
}