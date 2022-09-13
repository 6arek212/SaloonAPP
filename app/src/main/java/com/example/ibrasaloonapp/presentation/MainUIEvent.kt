package com.example.ibrasaloonapp.presentation

import com.example.ibrasaloonapp.domain.model.AuthData

sealed class MainUIEvent {

    class AuthDataReady(val isAuthed: Boolean) : MainUIEvent()
    class LoggedIn(val authData: AuthData) : MainUIEvent()
    object Logout : MainUIEvent()
    class NavigateNow(val route: String) : MainUIEvent()

}