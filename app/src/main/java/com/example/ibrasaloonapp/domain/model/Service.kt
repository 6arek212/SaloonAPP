package com.example.ibrasaloonapp.domain.model

import com.google.gson.annotations.SerializedName

data class Service(
    val id: String? = null,
    val title: String,
    val price: Int,
)