package com.example.ibrasaloonapp.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri


fun navigateToWaze(context: Context) {
    try {
        // Launch Waze to look for Hawaii:
        val url = "https://waze.com/ul?ll=40.761043,-73.980545&navigate=yes"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
        // If Waze is not installed, open it in Google Play:
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"))
        context.startActivity(intent)
    }
}


fun navigateToMaps(context: Context) {
    val uri = "geo: latitude,longtitude"
    context.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(uri)
        )
    )
}


fun navigateToWhatsapp(context: Context, phone: String) {
    val uri = "https://api.whatsapp.com/send?phone=$phone"
    context.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(uri)
        )
    )
}