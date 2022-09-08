package com.example.ibrasaloonapp.presentation.ui.profile

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.User

data class ProfileState(
    val user: User? = null,
    ) {
}