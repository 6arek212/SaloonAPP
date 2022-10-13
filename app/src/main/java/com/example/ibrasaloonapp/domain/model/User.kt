package com.example.ibrasaloonapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val role: String,
    val image: String? = null,
    val superUser: Boolean? = null,
    val isBlocked: Boolean = false
) : Parcelable