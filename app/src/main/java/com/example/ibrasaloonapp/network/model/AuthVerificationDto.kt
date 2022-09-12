package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class AuthVerificationDto(
    @SerializedName("phone")
    val phone: String
) {
}