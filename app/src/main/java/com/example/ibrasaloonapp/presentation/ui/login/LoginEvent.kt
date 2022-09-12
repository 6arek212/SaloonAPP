package com.example.ibrasaloonapp.presentation.ui.login


enum class CodeDigitPlace {
    ONE, TWO, THREE, FOUR
}

sealed class LoginEvent {
    data class OnPhoneValueChange(val value: String) : LoginEvent()
    object OnRemoveHeadFromQueue : LoginEvent()
    object Reset : LoginEvent()
    class OnCodeDigitChanged(val codePlace: CodeDigitPlace, val value: String) : LoginEvent()
    object SendAuthVerification : LoginEvent()
    object Login : LoginEvent()
    object LoggedInStatus : LoginEvent()

}