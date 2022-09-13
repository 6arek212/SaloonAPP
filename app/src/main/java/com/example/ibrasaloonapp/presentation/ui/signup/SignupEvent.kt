package com.example.ibrasaloonapp.presentation.ui.signup

import com.example.ibrasaloonapp.presentation.ui.home.HomeEvent
import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace
import com.example.ibrasaloonapp.presentation.ui.login.LoginEvent


sealed class SignupEvent {
    object PrevPage :SignupEvent()
    object NextPage :SignupEvent()
    object Signup :SignupEvent()

    object SendAuthVerification : SignupEvent()

    class OnCodeDigitChanged(val codePlace: CodeDigitPlace, val value: String) : SignupEvent()
    class OnFirstNameChanged(val name:String) : SignupEvent()
    class OnLastNameChanged(val last:String) : SignupEvent()
    class OnPhoneChanged(val phone:String) : SignupEvent()
    class OnBirthDateChanged(val birthdate:String) : SignupEvent()

    object OnRemoveHeadFromQueue : SignupEvent()

}