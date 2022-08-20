package com.example.ibrasaloonapp.domain.user_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
) {
}