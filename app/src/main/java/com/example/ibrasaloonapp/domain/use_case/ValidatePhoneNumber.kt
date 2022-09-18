package com.example.ibrasaloonapp.domain.use_case

import android.app.Application
import android.util.Patterns
import com.example.ibrasaloonapp.R
import javax.inject.Inject

class ValidatePhoneNumber
@Inject
constructor(val context: Application) {

    fun execute(phone: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(
                    R.string.the_filed_cant_be_blank,
                    context.getString(R.string.phone)
                )
            )
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.the_filed_is_not_valid , context.getString(R.string.phone))
            )
        }


        return ValidationResult(successful = true)
    }

}