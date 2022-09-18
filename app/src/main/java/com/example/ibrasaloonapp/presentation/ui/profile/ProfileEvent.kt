package com.example.ibrasaloonapp.presentation.ui.profile



sealed class ProfileEvent {
    object GetUser : ProfileEvent()
    object OnRemoveUIComponent : ProfileEvent()
    class UpdateImage(val imagePath: String) : ProfileEvent()
}