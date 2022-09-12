package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

interface AuthRepository {


    suspend fun sendAuthVerification(phone: String): ApiResult<String>

    suspend fun login(loginDataDto: LoginDataDto): ApiResult<AuthData>

    suspend fun signup(): ApiResult<String>

    suspend fun getLoginStatus(): AuthData?

    suspend fun logout()

}