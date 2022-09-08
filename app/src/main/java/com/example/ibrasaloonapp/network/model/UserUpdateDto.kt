package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class UserUpdateDto(
    @SerializedName("firstName")
    val firstName: String? = null,

    @SerializedName("lastName")
    val lastName: String? = null,

    @SerializedName("phone")
    val phone: String? = null,


    ) {
}