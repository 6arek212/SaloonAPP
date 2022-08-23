package com.example.ibrasaloonapp.domain.use_case

import android.util.Patterns

class ValidatePhoneNumber {

    fun execute(phone: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult(successful = false, errorMessage = "The phone number can't be blank")
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            return ValidationResult(successful = false, errorMessage = "The phone number is not valid")
        }


        return ValidationResult(successful = true)
    }

}