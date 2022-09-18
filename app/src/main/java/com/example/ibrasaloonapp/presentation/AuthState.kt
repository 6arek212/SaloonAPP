package com.example.ibrasaloonapp.presentation

import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User

data class AuthState(
    var isLoggedIn: Boolean = false,
    val authData: AuthData? = null,
) {
}