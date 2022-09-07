package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.UserDto

data class UserResponse(
    val message: String,
    val user: UserDto
) {
}