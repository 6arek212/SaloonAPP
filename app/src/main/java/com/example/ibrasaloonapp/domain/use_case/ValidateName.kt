package com.example.ibrasaloonapp.domain.use_case


class ValidateName {

    fun execute(fieldKey: String, value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The ${fieldKey} can't be blank"
            )
        }

        if (value.matches("^[a-z\\u0590-\\u05fe\\u0621-\\u064A]+\$".toRegex())) {
            return ValidationResult(
                successful = false,
                errorMessage = "Name must contain letters only"
            )
        }


        return ValidationResult(successful = true)
    }

}