package com.example.ibrasaloonapp.presentation


sealed class MainUIEvent {
    object AuthDataReady : MainUIEvent()
    object LoggedIn : MainUIEvent()
    object Logout : MainUIEvent()
    object Nothing : MainUIEvent()
}