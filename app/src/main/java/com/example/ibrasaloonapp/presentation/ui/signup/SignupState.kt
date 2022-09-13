package com.example.ibrasaloonapp.presentation.ui.signup

import com.example.ibrasaloonapp.domain.model.OPT4Digits

data class SignupState(
    val phone: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val verifyCode: OPT4Digits = OPT4Digits(""),

    val phoneError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val birthDateError: String? = null,

    val page: Int = 1,
    val showCode: Boolean = false

) {
}