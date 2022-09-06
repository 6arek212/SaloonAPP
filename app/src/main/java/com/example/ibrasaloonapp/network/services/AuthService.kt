package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("/login")
    suspend fun login(@Body data: LoginDataDto): LoginResponse


    @POST("/signup")
    suspend fun signup()


    @POST("/refresh-token")
    suspend fun refreshToken(@Query("token") token: String)


}