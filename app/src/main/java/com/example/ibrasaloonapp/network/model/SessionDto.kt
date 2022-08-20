package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class SessionDto(

    @SerializedName("id")
    val id: String? = null,


    @SerializedName("type")
    val type: String? = null,

    @SerializedName("date")
    val date: String? = null
) {
}