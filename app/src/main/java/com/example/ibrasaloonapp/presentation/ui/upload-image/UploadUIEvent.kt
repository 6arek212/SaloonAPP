package com.example.ibrasaloonapp.presentation.ui.upload


sealed class UploadUIEvent {
    class ImageUploaded(val imagePath: String) : UploadUIEvent()
}