package com.example.ibrasaloonapp.presentation

import com.example.ibrasaloonapp.domain.model.AuthData

sealed class AuthEvent {
    object Nothing : AuthEvent()
    object Logout : AuthEvent()
    class Login(val authData: AuthData) : AuthEvent()
}