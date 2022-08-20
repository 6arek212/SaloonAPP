package com.example.ibrasaloonapp.presentation.ui.session_list

import com.example.ibrasaloonapp.domain.model.ProgressBarState
import com.example.ibrasaloonapp.domain.model.Session
import com.example.ibrasaloonapp.domain.model.UIComponent
import java.util.*

data class SessionListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val sessions: List<Session> = listOf(),
    val errorQueue: Queue<UIComponent> = LinkedList()
) {
}