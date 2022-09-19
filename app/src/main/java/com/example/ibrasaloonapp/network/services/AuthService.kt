package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.*
import com.example.ibrasaloonapp.network.responses.*
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthService {

    @POST("send-auth-verification")
    suspend fun sendAuthVerification(@Body data: AuthVerificationDto): AuthVerificationResponse


    @POST("login-verify-phone")
    suspend fun loginAndVerifyPhone(@Body data: LoginDataDto): LoginResponse


    @POST("signup-verify-phone")
    suspend fun signupAndVerifyPhone(@Body data: SignupDataDto): SignupResponse


    @PATCH("verify-update-phone")
    suspend fun verifyAndUpdatePhone(@Body data: VerifyUpdatePhoneDto): MessageResponse


    @POST("refresh-token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenDto): RefreshTokenResponse


}