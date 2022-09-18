package com.example.ibrasaloonapp.domain.use_case

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import javax.inject.Inject

private const val TAG = "ValidateRequired"

class ValidateRequired
@Inject
constructor(
    val context: Application
) {

    fun execute(fieldKey: String, value: String): ValidationResult {
        if (value.trim().isBlank()) {
            Log.d(TAG, "execute: error")
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.the_filed_cant_be_blank ,fieldKey )
            )
        }
        return ValidationResult(true)
    }


}