package com.example.ibrasaloonapp.presentation.ui.user_details

import com.example.ibrasaloonapp.domain.model.User

data class UserDetailsState(
    val user: User? = null,
    val appointmentCount: Int? = null,
    val paid: Double? = null,
    val refresh: Boolean = false
) {
}