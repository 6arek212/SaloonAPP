package com.example.ibrasaloonapp.domain.use_case

import android.util.Log

private const val TAG = "ValidateRequired"

class ValidateRequired
constructor() {

    fun execute(fieldName: String, str: String): ValidationResult {
        if (str.trim().isBlank()) {
            Log.d(TAG, "execute: error")
            return ValidationResult(
                successful = false,
                errorMessage = "The ${fieldName} can't be blank"
            )
        }
        return ValidationResult(true)
    }


}