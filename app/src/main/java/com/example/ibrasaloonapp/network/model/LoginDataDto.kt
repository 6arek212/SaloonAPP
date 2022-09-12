package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class LoginDataDto(
    @SerializedName("phone")
    val phone: String,

    @SerializedName("verifyId")
    val verifyId: String,

    @SerializedName("code")
    val code: String
) {
}