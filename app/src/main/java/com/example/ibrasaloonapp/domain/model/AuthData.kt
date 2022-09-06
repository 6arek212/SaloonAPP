package com.example.ibrasaloonapp.domain.model

data class AuthData(
    val user: User,
    val token: String,
    val expiresIn: Int
) {
}