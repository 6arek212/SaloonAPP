package com.example.ibrasaloonapp.domain.model

import com.example.ibrasaloonapp.network.model.CustomerDto

data class AuthData(
    val customer: Customer,
    val token: String,
    val expiresIn: Int
) {
}