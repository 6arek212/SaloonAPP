package com.example.ibrasaloonapp.presentation.ui.signup

import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace


sealed class SignupEvent {
    object PrevPage : SignupEvent()
    object NextPage : SignupEvent()
    object Signup : SignupEvent()

    class SendAuthVerification(val sendAgain: Boolean) : SignupEvent()

    class OnCodeDigitChanged(val codePlace: CodeDigitPlace, val value: String) : SignupEvent()
    class OnFirstNameChanged(val name: String) : SignupEvent()
    class OnLastNameChanged(val last: String) : SignupEvent()
    class OnPhoneChanged(val phone: String) : SignupEvent()
    class OnBirthDateChanged(val birthdate: String) : SignupEvent()

    object RestPhoneSection : SignupEvent()
    object OnRemoveHeadFromQueue : SignupEvent()

}