package com.example.ibrasaloonapp.presentation

import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User

sealed class AuthEvent {
    object Nothing : AuthEvent()
    object Logout : AuthEvent()
    class Login(val authData: AuthData) : AuthEvent()
    class UpdateUser(val user: User) : AuthEvent()
}