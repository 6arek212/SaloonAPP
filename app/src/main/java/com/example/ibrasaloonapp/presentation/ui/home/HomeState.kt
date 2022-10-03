package com.example.ibrasaloonapp.presentation.ui.home

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User

data class HomeState(
    val appointment: Appointment? = null,
    val workers: List<User> = listOf(),
    val refreshing: Boolean = false
) {
}