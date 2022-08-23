package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun getLoginStatus(): AuthData

    suspend fun login(loginDataDto: LoginDataDto): ApiResult<String>

    suspend fun signup(): ApiResult<String>


}