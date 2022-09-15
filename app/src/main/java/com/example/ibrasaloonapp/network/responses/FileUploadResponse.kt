package com.example.ibrasaloonapp.network.responses

import com.google.gson.annotations.SerializedName

class FileUploadResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("filename")
    val fileName: String
)