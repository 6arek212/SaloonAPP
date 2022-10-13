package com.example.ibrasaloonapp.network.model

import com.google.gson.annotations.SerializedName

data class UserUpdateDto(
    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("firstName")
    val firstName: String? = null,

    @SerializedName("lastName")
    val lastName: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("superUser")
    val superUser: Boolean? = null,

    @SerializedName("isBlocked")
    val isBlocked: Boolean? = null
)