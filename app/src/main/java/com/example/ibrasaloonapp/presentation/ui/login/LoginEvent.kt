package com.example.ibrasaloonapp.presentation.ui.login


sealed class LoginEvent {
    data class OnPhoneValueChange(val value: String) : LoginEvent()
    data class OnPasswordValueChange(val value: String) : LoginEvent()
    object OnRemoveHeadFromQueue : LoginEvent()

    object Login : LoginEvent()
    object LoggedInStatus : LoginEvent()

}