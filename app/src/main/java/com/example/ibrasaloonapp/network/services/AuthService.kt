package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body data: LoginDataDto): LoginResponse


    @POST("auth/signup")
    suspend fun signup()


    @POST("auth/refresh-token")
    suspend fun refreshToken(@Query("token") token: String)


}