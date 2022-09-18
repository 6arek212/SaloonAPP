package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.model.SignupDataDto
import com.example.ibrasaloonapp.presentation.AuthEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

interface AuthRepository {


    suspend fun sendAuthVerification(
        phone: String,
        forLogin: Boolean? = null,
        forSignup: Boolean? = null
    ): ApiResult<String>

    suspend fun login(loginDataDto: LoginDataDto): ApiResult<AuthData>

    suspend fun signup(signupDataDto: SignupDataDto): ApiResult<AuthData>

    suspend fun getCacheAuthData(updateStatus: Boolean = false): AuthData?

    suspend fun getAuthFlow(): Flow<AuthEvent>

    fun getUserId(): String?

    suspend fun logout()

    suspend fun refreshToken(): String?

    suspend fun removeCurrentToken()

    suspend fun updateUserData(user: User)

    suspend fun updateUserImage(path: String)


}