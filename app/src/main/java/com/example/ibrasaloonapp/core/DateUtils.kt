package com.example.ibrasaloonapp.core

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "DateUtils"

//"yyyy-MM-dd'T'HH:mm:ssZZZZZ"
//yyyy-MM-dd'T'HH:mm:ssZ

/*
val c = Calendar.getInstance()
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
 */

fun getCurrentDateAsString(): String {
    val c = Calendar.getInstance()
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(c.time)
    return date
}

fun getDateAsString(): String {
    val c = Calendar.getInstance()
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(c.time)
    return date
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
        val sdfOutput = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val formatted = sdfOutput.format(date)
        return formatted.toString()
    } catch (e: ParseException) {
        // handle the failure
        Log.e(TAG, "formatDate:${e.message} ")
        return ""
    }
}


fun stringDateToTimeFormat(str: String): String {
    try {
        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val date = sdfInput.parse(str)
        val sdfOutput = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formatted = sdfOutput.format(date)
        return formatted.toString()
    } catch (e: ParseException) {
        // handle the failure
        Log.e(TAG, "formatDate:${e.message} ")
        return ""
    }
}


fun stringDateToDateFormat(str: String): String {
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