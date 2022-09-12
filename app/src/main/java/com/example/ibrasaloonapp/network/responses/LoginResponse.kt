package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.AuthDataDto
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("authData")
    val authDataDto: AuthDataDto
) {
}