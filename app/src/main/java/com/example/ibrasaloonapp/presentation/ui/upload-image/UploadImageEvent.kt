package com.example.ibrasaloonapp.presentation.ui.upload

import android.net.Uri
import java.io.InputStream


sealed class UploadImageEvent {
    object OnRemoveUIComponent : UploadImageEvent()
    object Upload : UploadImageEvent()
    class OnSelectedImage(val imageStream: InputStream?, val imageUri: Uri?) : UploadImageEvent()

}