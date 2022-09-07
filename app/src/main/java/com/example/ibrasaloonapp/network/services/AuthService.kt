package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.AuthDataDto
import com.example.ibrasaloonapp.network.model.LoginDataDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("login")
    suspend fun login(@Body data: LoginDataDto): AuthDataDto


    @POST("signup")
    suspend fun signup()


    @POST("refresh-token")
    suspend fun refreshToken(@Query("token") token: String)


}