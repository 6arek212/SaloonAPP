package com.example.ibrasaloonapp.domain.model

import com.google.gson.annotations.SerializedName

data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phone: String

) {
}