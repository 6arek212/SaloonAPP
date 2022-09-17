package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.trainingapp.network.NetworkErrors
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "LoginUseCase"

class LoginUseCase
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val context: Application
) {

    suspend operator fun invoke(phone: String, verifyId: String, code: String) = flow {
        emit(Resource.Loading(true))


        val result = authRepository.login(
            LoginDataDto(
                phone = phone,
                verifyId = verifyId,
                code = code
            )
        )

        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "invoke: logged in")
                emit(Resource.Success(result.value))
            }

            is ApiResult.GenericError -> {
                val message = when (result.code) {

                    NetworkErrors.ERROR_404 -> {
                        context.getString(R.string.verification_timeout_try_again)
                    }

                    NetworkErrors.ERROR_403 -> {
                        context.getString(R.string.code_not_match)
                    }

                    else -> {
                        context.getString(R.string.something_went_wrong)
                    }
                }

                emit(Resource.Error(message = message))
            }

            is ApiResult.NetworkError -> {
                emit(Resource.Error(message = context.getString(R.string.something_went_wrong)))
            }
        }

        emit(Resource.Loading(false))
    }
}