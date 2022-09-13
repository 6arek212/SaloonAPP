package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class SignupDataDto(

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("birthDate")
    val birthDate: String,

    @SerializedName("verifyId")
    val verifyId: String,

    @SerializedName("code")
    val code: String

    ) {
}