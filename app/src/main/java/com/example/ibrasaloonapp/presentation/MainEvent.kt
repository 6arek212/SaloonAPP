package com.example.ibrasaloonapp.presentation

import com.example.ibrasaloonapp.domain.model.AuthData

sealed class MainEvent {

    class Login(val authData: AuthData) : MainEvent()
    object Logout : MainEvent()
    object GetAuthData : MainEvent()
    object RemoveMessage : MainEvent()
    object ShowLogoutDialog : MainEvent()
    object DismissNetworkMessage : MainEvent()
}