package com.example.ibrasaloonapp.presentation.ui.home

import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.Queue
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.Appointment

data class HomeState(
    val appointment: Appointment? = null,
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())
) {
}