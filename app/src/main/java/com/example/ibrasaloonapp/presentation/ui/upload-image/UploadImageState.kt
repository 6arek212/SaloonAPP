package com.example.ibrasaloonapp.presentation.ui.upload

import android.net.Uri


data class UploadImageState(
    val imageUri: Uri? = null,
    val buttonVisible: Boolean = false,
)