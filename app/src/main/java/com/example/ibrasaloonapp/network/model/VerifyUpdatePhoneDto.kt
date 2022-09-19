package com.example.ibrasaloonapp.network.model

data class VerifyUpdatePhoneDto(
    val phone: String,
    val verifyId: String,
    val code: String,
    val userId: String
) {
}