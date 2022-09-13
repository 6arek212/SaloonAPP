package com.example.ibrasaloonapp.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.edit
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.AuthDataDtoMapper
import com.example.ibrasaloonapp.network.model.AuthVerificationDto
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.model.RefreshTokenDto
import com.example.ibrasaloonapp.network.services.AuthService
import com.example.ibrasaloonapp.presentation.ui.login.LoginViewModel
import com.example.ibrasaloonapp.ui.*
import com.example.trainingapp.util.safeApiCall
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl
@Inject
constructor(
    private val application: Context,
    private val authService: AuthService,
    private val authDataDtoMapper: AuthDataDtoMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {


    override suspend fun getLoginStatus(): AuthData? {
        val authData = application.dataStore.data.first().getAuthData()
        authData?.let {
            if (it.token.isEmpty()) {
                return null
            }
        }
        return authData
    }

    override suspend fun sendAuthVerification(phone: String): ApiResult<String> {
        return safeApiCall(dispatcher) {
            authService.sendAuthVerification(AuthVerificationDto(phone = phone)).verifyId
        }
    }

    override suspend fun login(loginDataDto: LoginDataDto): ApiResult<AuthData> {
        val result = safeApiCall(dispatcher) {
            val res = authService.loginAndVerifyPhone(loginDataDto).authDataDto
            Log.d(TAG, "login: $res")
            authDataDtoMapper.mapToDomainModel(res)
        }

        return when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "login: logged in success ${result.value}")
                application.dataStore.edit { settings ->
                    settings.insertAuthData(result.value)
                }
                ApiResult.Success(result.value)
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "login: logged in fail")
                ApiResult.GenericError(
                    code = result.code,
                    errorMessage = result.errorMessage
                )
            }

            is ApiResult.NetworkError -> {
                ApiResult.NetworkError
            }
        }
    }


    override suspend fun logout() {
        application.dataStore.edit { settings ->
            settings.clearAuthData()
        }
    }

    override suspend fun signup(): ApiResult<String> {
        TODO("Not yet implemented")
    }


    override suspend fun refreshToken(): String? {
        val authData = application.dataStore.data.first().getAuthData()

        Log.d(TAG, "refreshToken: ${authData}")

        if (authData != null && !authData.refreshToken.isEmpty()) {

            val result = safeApiCall(dispatcher = dispatcher) {
                authService.refreshToken(RefreshTokenDto(refreshToken = authData.refreshToken)).token
            }

            return when (result) {
                is ApiResult.Success -> {
                    application.dataStore.edit { settings ->
                        settings[TOKEN] = result.value
                    }
                    result.value
                }

                is ApiResult.GenericError -> {
                    null
                }

                is ApiResult.NetworkError -> {
                    null
                }
            }
        }

        return null
    }

    override suspend fun removeCurrentToken() {
        application.dataStore.edit { settings ->
            settings[TOKEN] = ""
        }
    }
}