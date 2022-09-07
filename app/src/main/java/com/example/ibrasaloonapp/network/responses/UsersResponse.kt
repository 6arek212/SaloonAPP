package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.UserDto

data class UsersResponse(
    val message: String,
    val users: List<UserDto>
) {
}