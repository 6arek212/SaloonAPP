package com.example.ibrasaloonapp.domain.use_case

import android.app.Application
import android.util.Patterns
import com.example.ibrasaloonapp.R
import javax.inject.Inject


interface Validator {
    fun validate(fieldKey: String, value: String, context: Application): ValidationResult
}

private const val MAX_LENGTH = 16
private const val MIN_LENGTH = 2

sealed class Validators : Validator {


    object Required : Validators() {
        override fun validate(
            fieldKey: String,
            value: String,
            context: Application
        ): ValidationResult {
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

            return ValidationResult(true)
        }
    }


    class MaxLength(val max: Int) : Validators() {
        override fun validate(
            fieldKey: String,
            value: String,
            context: Application
        ): ValidationResult {

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

            return ValidationResult(true)
        }
    }


    class MinLength(val min: Int) : Validators() {
        override fun validate(
            fieldKey: String,
            value: String,
            context: Application
        ): ValidationResult {
            if (fieldKey.length < min) {
                return ValidationResult(
                    successful = false,
                    errorMessage = context.getString(R.string.the_filed_cant_be_blank, fieldKey)
                )
            }
            return ValidationResult(true)
        }
    }


    object Email : Validators() {
        override fun validate(
            fieldKey: String,
            value: String,
            context: Application
        ): ValidationResult {
            if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                return ValidationResult(successful = false, errorMessage = "The email is not valid")
            }

            return ValidationResult(true)
        }
    }


    object Name : Validators() {
        override fun validate(
            fieldKey: String,
            value: String,
            context: Application
        ): ValidationResult {
            if (!value.matches("^[a-z\\u0590-\\u05fe\\u0621-\\u064A]+( [a-z\\u0590-\\u05fe\\u0621-\\u064A]+)*[ ]*\$".toRegex())) {
                return ValidationResult(
                    successful = false,
                    errorMessage = context.getString(
                        R.string.field_must_contain_letters_spaces,
                        fieldKey
                    )
                )
            }
            return ValidationResult(true)
        }
    }


}


class FieldValidator
@Inject
constructor(val context: Application) {

    fun validate(fieldKey: String, value: String, validators: List<Validators>): ValidationResult {
        for (validator in validators) {
            val v = validator.validate(fieldKey, value, context)
            if (!v.successful) {
                return v
            }
        }
        return ValidationResult(true)
    }

}