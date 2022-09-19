package com.example.ibrasaloonapp.presentation.ui.edit_profile

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.domain.model.User

data class EditProfileState(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val phone: String = "",

    val phoneError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val birthDateError: String? = null,

    val showCode: Boolean = false,
    val code: OPT4Digits = OPT4Digits("")
)