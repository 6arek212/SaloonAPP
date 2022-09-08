package com.example.ibrasaloonapp.presentation.ui.edit_profile

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.domain.model.User

data class EditProfileState(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
) {
}