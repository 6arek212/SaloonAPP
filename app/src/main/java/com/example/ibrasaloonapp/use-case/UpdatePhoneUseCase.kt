package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.network.model.VerifyUpdatePhoneDto
import com.example.ibrasaloonapp.presentation.ui.edit_profile.EditProfileViewModel
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.UserRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.trainingapp.network.NetworkErrors
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "UpdatePhoneUseCase"

class UpdatePhoneUseCase
@Inject
constructor(
    private val context: Application,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {


    suspend operator fun invoke(
        phone: String,
        verifyId: String,
        code: String,
        userId: String
    ) = flow {
        emit(Resource.Loading(true))


        val dto =
            VerifyUpdatePhoneDto(
                phone = phone,
                verifyId = verifyId,
                code = code,
                userId = userId
            )


        val result = authRepository.verifyAndUpdatePhone(dto)
        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "invoke: updated phone")
                emit(Resource.Success(data = result.value))
            }
            is ApiResult.GenericError -> {
                val message = when (result.code) {

                    NetworkErrors.ERROR_404 -> {
                        context.getString(R.string.verification_timeout_try_again)
                    }

                    NetworkErrors.ERROR_403 -> {
                        context.getString(R.string.code_not_match)
                    }

                    NetworkErrors.ERROR_400 -> {
                        context.getString(R.string.user_with_this_number_already_exists)
                    }

                    else -> {
                        context.getString(R.string.something_went_wrong)
                    }
                }
                if (result.code == 401) {
                    authRepository.logout()
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