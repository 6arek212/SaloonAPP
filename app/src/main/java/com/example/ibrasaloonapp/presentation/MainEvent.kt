package com.example.ibrasaloonapp.presentation


sealed class MainEvent {
    object Logout : MainEvent()
    object GetAuthData : MainEvent()
    object RemoveMessage : MainEvent()
    object ShowLogoutDialog : MainEvent()
    object DismissNetworkMessage : MainEvent()
}