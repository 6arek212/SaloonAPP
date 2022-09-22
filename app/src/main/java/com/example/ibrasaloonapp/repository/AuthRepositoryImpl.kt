package com.example.ibrasaloonapp.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User
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
    val authData: StateFlow<AuthEvent> = _authData

    private var user: User? = null

    override suspend fun getAuthFlow(): StateFlow<AuthEvent> {
        return authData
    }


    override fun getUserId(): String? {
        return user?.id
    }

    override suspend fun updateUserImage(path: String) {
        user?.let {
            _authData.emit(AuthEvent.UpdateUser(it.copy(image = path)))
            application.dataStore.edit { settings ->
                settings[USER_IMAGE] = path
            }
        }
    }

    override suspend fun updateUserData(user: User) {
        this.user?.let {
            if (user.id == it.id) {
                application.dataStore.edit { settings ->
                    settings.insertUser(user)
                }
                _authData.emit(AuthEvent.UpdateUser(user))
            }
        }
    }

    override suspend fun getCacheAuthData(updateStatus: Boolean): AuthData? {
        val authData = application.dataStore.data.first().getAuthData()
        authData?.let {
            if (it.token.isEmpty()) {
                return null
            }
            user = it.user

            if (updateStatus) {
                _authData.emit(AuthEvent.Login(it))
            }
        }
        return authData
    }

    override suspend fun sendAuthVerification(
        phone: String,
        forLogin: Boolean?,
        forSignup: Boolean?
    ): ApiResult<String> {
        return safeApiCall(dispatcher) {
            authService.sendAuthVerification(
                AuthVerificationDto(
                    phone = phone,
                    isLogin = forLogin,
                    isSignup = forSignup
                )
            ).verifyId
        }
    }


    override suspend fun verifyAndUpdatePhone(dto: VerifyUpdatePhoneDto): ApiResult<String> {
        return safeApiCall(dispatcher) {
            val msg = authService.verifyAndUpdatePhone(dto).message
            user?.let {
                _authData.emit(AuthEvent.UpdateUser(it.copy(phone = dto.phone)))
            }
            application.dataStore.edit { settings ->
                settings[PHONE] = dto.phone
            }
            msg
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
                user = result.value.user
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
        user = null
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
                user = result.value.user
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
                    logout()
                    null
                }

                is ApiResult.NetworkError -> {
                    logout()
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