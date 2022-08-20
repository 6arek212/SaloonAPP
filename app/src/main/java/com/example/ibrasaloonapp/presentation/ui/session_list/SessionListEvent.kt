package com.example.ibrasaloonapp.presentation.ui.session_list

sealed class SessionListEvent {
    object GetSessions : SessionListEvent()
    object OnRemoveHeadFromQueue: SessionListEvent()
}