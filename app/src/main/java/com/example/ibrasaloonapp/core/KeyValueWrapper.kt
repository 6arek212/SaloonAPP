package com.example.ibrasaloonapp.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class KeyValueWrapper(
    val key: String,
    var value: String):Parcelable