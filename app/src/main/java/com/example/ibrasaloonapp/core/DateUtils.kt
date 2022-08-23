package com.example.ibrasaloonapp.core

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "DateUtils"

fun getCurrentDateAsString(): String {
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
}


fun stringToDate(str: String): String {
    try {
        val sdfInput = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = sdfInput.parse(str)
        val sdfOutput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatted = sdfOutput.format(date)
        return formatted.toString()
    } catch (e: ParseException) {
        // handle the failure
        Log.e(TAG, "formatDate:${e.message} ")
        return ""
    }
}

fun stringDateFormat(str: String): String {
    try {
        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val date = sdfInput.parse(str)
        val sdfOutput = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val formatted = sdfOutput.format(date)
        return formatted.toString()
    } catch (e: ParseException) {
        // handle the failure
        Log.e(TAG, "formatDate:${e.message} ")
        return ""
    }
}