package com.example.ibrasaloonapp.presentation.ui.login

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.OPT4Digits

data class LoginState(
    val phone: String = "",
    val phoneError: String? = null,
    val verifyCode: OPT4Digits = OPT4Digits("", "", "", ""),
    val showCode: Boolean = false
) {
}