package com.example.ibrasaloonapp.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.*
import com.example.ibrasaloonapp.network.services.AuthService
import com.example.ibrasaloonapp.presentation.AuthEvent
import com.example.ibrasaloonapp.ui.*
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl
@Inject
constructor(
    private val application: Context,
    private val authService: AuthService,
    private val authDataDtoMapper: AuthDataDtoMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {

    private val _authData = MutableStateFlow<AuthEvent>(AuthEvent.Nothing)
    val authData: Flow<AuthEvent> = _authData

    private var userId: String? = null

    override suspend fun getAuthFlow(): Flow<AuthEvent> {
        return authData
    }


    override fun getUserId(): String? {
        return userId
    }

    override suspend fun getCacheAuthData(): AuthData? {
        val authData = application.dataStore.data.first().getAuthData()
        authData?.let {
            if (it.token.isEmpty()) {
                return null
            }
            userId = it.user.id
            _authData.emit(AuthEvent.Login(it))
        }
        return authData
    }

    override suspend fun sendAuthVerification(
        phone: String,
        forLogin: Boolean?
    ): ApiResult<String> {
        return safeApiCall(dispatcher) {
            authService.sendAuthVerification(
                AuthVerificationDto(
                    phone = phone,
                    isLogin = forLogin
                )
            ).verifyId
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
                userId = result.value.user.id
                _authData.emit(AuthEvent.Login(result.value))
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
        userId = null
        application.dataStore.edit { settings ->
            settings.clearAuthData()
        }
        _authData.emit(AuthEvent.Logout)
    }

    override suspend fun signup(signupDataDto: SignupDataDto): ApiResult<AuthData> {
        val result = safeApiCall(dispatcher) {
            val res = authService.signupAndVerifyPhone(signupDataDto).authDataDto
            Log.d(TAG, "login: $res")
            authDataDtoMapper.mapToDomainModel(res)
        }

        return when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "login: logged in success ${result.value}")
                application.dataStore.edit { settings ->
                    settings.insertAuthData(result.value)
                }
                userId = result.value.user.id
                _authData.emit(AuthEvent.Login(result.value))
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