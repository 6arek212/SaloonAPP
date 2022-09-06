package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("phone")
    val phone: String
) {
}