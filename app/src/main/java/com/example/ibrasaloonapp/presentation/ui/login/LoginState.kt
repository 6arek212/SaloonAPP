package com.example.ibrasaloonapp.presentation.ui.login

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent

data class LoginState(
    val phone: String = "",
    val password: String = "",
    val phoneError: String? = null,
    val passwordError: String? = null,
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())
) {
}