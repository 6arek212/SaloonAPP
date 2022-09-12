package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.AuthVerificationDto
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.responses.AuthVerificationResponse
import com.example.ibrasaloonapp.network.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("send-auth-verification")
    suspend fun sendAuthVerification(@Body data: AuthVerificationDto): AuthVerificationResponse


    @POST("login-verify-phone")
    suspend fun loginAndVerifyPhone(@Body data: LoginDataDto) : LoginResponse


    @POST("signup-verify-phone")
    suspend fun signupAndVerifyPhone()


    @POST("verify-phone")
    suspend fun verifyPhone()


}