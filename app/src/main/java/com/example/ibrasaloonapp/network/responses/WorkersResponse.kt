package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.UserDto

data class WorkersResponse(
    val message: String,
    val workers: List<UserDto>
) {
}