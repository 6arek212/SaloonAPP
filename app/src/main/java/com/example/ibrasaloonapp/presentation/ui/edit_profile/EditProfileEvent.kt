package com.example.ibrasaloonapp.presentation.ui.edit_profile

import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace

sealed class EditProfileEvent {
    class OnFirstNameChanged(val name: String) : EditProfileEvent()
    class OnLastNameChanged(val last: String) : EditProfileEvent()
    class OnPhoneChanged(val phone: String) : EditProfileEvent()
    object UpdateProfile :EditProfileEvent()
    object OnRemoveHeadFromQueue : EditProfileEvent()
    object SendAuthVerification : EditProfileEvent()
    class OnCodeDigitChanged(val codePlace: CodeDigitPlace, val value: String) : EditProfileEvent()
    object OnRestCode : EditProfileEvent()

}