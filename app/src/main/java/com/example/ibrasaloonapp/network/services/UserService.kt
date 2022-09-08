package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.model.AuthDataDto
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.model.UserDto
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.network.responses.MessageResponse
import com.example.ibrasaloonapp.network.responses.UserResponse
import com.example.ibrasaloonapp.network.responses.UsersResponse
import retrofit2.http.*

interface UserService {

    @GET("users")
    suspend fun getUsers(): UsersResponse


    @GET("users/{:id}")
    suspend fun getUser(): UserResponse


    @PATCH("users")
    suspend fun updateUser(@Body user: UserUpdateDto): UserResponse


    @DELETE("users/{:id}")
    suspend fun deleteUser(): MessageResponse

}