package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class RefreshTokenDto(
    @SerializedName("refreshToken")
    val refreshToken: String
) {
}