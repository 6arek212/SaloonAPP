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
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.services.AuthService
import com.example.ibrasaloonapp.presentation.ui.login.LoginViewModel
import com.example.ibrasaloonapp.ui.dataStore
import com.example.ibrasaloonapp.ui.getAuthData
import com.example.ibrasaloonapp.ui.insertAuthData
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


    override suspend fun getLoginStatus(): AuthData {
        return application.dataStore.data.first().getAuthData()
    }

    override suspend fun login(loginDataDto: LoginDataDto): ApiResult<String> {
        val result = safeApiCall(dispatcher) {
            val res = authService.login(loginDataDto).authDataDto
            Log.d(TAG, "login: ${res}")
            authDataDtoMapper.mapToDomainModel(res)
        }


        return when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "login: logged in success")
                application.dataStore.edit { settings ->
                    settings.insertAuthData(result.value)
                }
                ApiResult.Success("Logged in success")
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

    override suspend fun signup(): ApiResult<String> {
        TODO("Not yet implemented")
    }
}