package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class AuthDataDto(
    @SerializedName("user")
    val user: UserDto,

    @SerializedName("token")
    val token: String,

    @SerializedName("expiresIn")
    val expiresIn: Int
) {
}