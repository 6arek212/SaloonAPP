package com.example.ibrasaloonapp.presentation

import com.example.ibrasaloonapp.domain.model.AuthData

data class AuthState(
    var isLoggedIn: Boolean = false,
    val authData: AuthData? = null
) {
}