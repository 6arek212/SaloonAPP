package com.example.ibrasaloonapp.core

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import com.example.ibrasaloonapp.R
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


//fun stringDateFormat(str: String): String {
//    try {
//        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
//        val date = sdfInput.parse(str)
//        val sdfOutput = SimpleDateFormat("MMM dd, yyyy HH:mmZ", Locale.getDefault())
//        val formatted = sdfOutput.format(date)
//        return formatted.toString()
//    } catch (e: ParseException) {
//        // handle the failure
//        Log.e(TAG, "formatDate:${e.message} ")
//        return ""
//    }
//}
//
//
//fun stringDateToTimeFormat(str: String): String {
//    try {
//        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
//        val date = sdfInput.parse(str)
//        val sdfOutput = SimpleDateFormat("HH:mm", Locale.getDefault())
//        sdfOutput.timeZone = TimeZone.getDefault()
//        val formatted = sdfOutput.format(date)
//        return formatted.toString()
//    } catch (e: ParseException) {
//        // handle the failure
//        Log.e(TAG, "formatDate:${e.message} ")
//        return ""
//    }
//}

enum class TimePatterns(val value: String) {
    DAY_ONLY("EEEE"),
    MONTH_ONLY("MMMM"),
    TIME_ONLY("HH:mm"),
    DATE_MM_DD("MMM dd"),
    REGULAR_DATE("dd/MM/yyyy"),
    DATE_MMM_DD_YYYY("MMM dd, yyyy"),
    EEEE_MM_DD("EEEE, MMM dd"),
    DATE_TIME("EEEE MMM dd, yyyy HH:mm")
}


fun stringDateFormat(
    str: String,
    pattern: TimePatterns = TimePatterns.DATE_MM_DD,
    context: Context
): String {
    try {

        Log.d(TAG, "stringDateFormat: input ${str}")

        val readDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        readDate.timeZone = TimeZone.getTimeZone("UTC");
        val date = readDate.parse(str) ?: throw Exception("Error parsing date")

        val writeDate = SimpleDateFormat(pattern.value, Locale.getDefault())
        val s = writeDate.format(date)
        val ssDate = writeDate.parse(s) ?: throw Exception("Error parsing date")

        val c1 = Calendar.getInstance()
        c1.timeInMillis = ssDate.time
        val now = Calendar.getInstance()

        if (pattern == TimePatterns.DATE_MM_DD || pattern == TimePatterns.DATE_MMM_DD_YYYY) {
            if (now.get(Calendar.DATE) == c1.get(Calendar.DATE)
            ) {
                return context.getString(R.string.today)
            } else if (c1.get(Calendar.DATE) - now.get(Calendar.DATE) == 1) {
                return context.getString(R.string.tomorrow)
            } else if (now.get(Calendar.DATE) - c1.get(Calendar.DATE) == 1) {
                return context.getString(R.string.yesterday)
            }
        }
        Log.d(TAG, "stringDateFormat: output ${s}")

        return s
    } catch (e: ParseException) {
        // handle the failure
        Log.e(TAG, "formatDate:${e.message} ")
        return ""
    }
}