package com.example.ibrasaloonapp.ui

import android.content.Context
import com.example.ibrasaloonapp.R
import com.example.trainingapp.network.NetworkErrors


fun Int?.defaultErrorMessage(context: Context): String {
    return when (this) {
        NetworkErrors.ERROR_404 -> {
            context.getString(R.string.not_found)
        }
        NetworkErrors.ERROR_400 -> {
            context.getString(R.string.bad_request)
        }
        NetworkErrors.ERROR_401 -> {
            context.getString(R.string.not_authorized)
        }

        else -> {
            context.getString(R.string.something_went_wrong)
        }
    }
}