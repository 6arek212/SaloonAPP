package com.example.ibrasaloonapp.presentation.ui.profile

import com.example.ibrasaloonapp.domain.model.User


sealed class ProfileEvent {
    object GetUser : ProfileEvent()
    object OnRemoveUIComponent : ProfileEvent()
    class UpdateUser(val user: User) : ProfileEvent()
    class UpdateImage(val imagePath: String) : ProfileEvent()
}