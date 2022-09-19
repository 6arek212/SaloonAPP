package com.example.ibrasaloonapp.domain.use_case

import android.app.Application
import com.example.ibrasaloonapp.R
import javax.inject.Inject

private const val MAX_LENGTH = 16
private const val MIN_LENGTH = 2

class ValidateName
@Inject
constructor(
    val context: Application
) {


    fun execute(fieldKey: String, value: String): ValidationResult {
        if (value.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.the_filed_cant_be_blank, fieldKey)
            )
        }

        if (value.length > MAX_LENGTH) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(
                    R.string.the_filed_cant_be_longer_than,
                    fieldKey,
                    MAX_LENGTH
                )
            )
        }

        if (value.length < MIN_LENGTH) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(
                    R.string.the_filed_cant_be_less_than,
                    fieldKey,
                    MIN_LENGTH
                )
            )
        }


        if (!value.matches("^[a-zA-Z\\u0590-\\u05fe\\u0621-\\u064A]+( [a-zA-Z\\u0590-\\u05fe\\u0621-\\u064A]+)*[ ]*\$".toRegex())) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(
                    R.string.field_must_contain_letters_spaces,
                    fieldKey
                )
            )
        }


        return ValidationResult(successful = true)
    }

}