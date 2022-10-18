package com.example.ibrasaloonapp.presentation.ui

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.UIComponent

data class UIState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val uiMessage: UIComponent? = null,
    val network: Boolean? = null
) {
}