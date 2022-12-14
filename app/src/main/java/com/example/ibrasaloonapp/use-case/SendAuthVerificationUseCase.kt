package com.example.ibrasaloonapp.use

import android.app.Application
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.trainingapp.network.NetworkErrors
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "SendAuthVerificationUse"

class SendAuthVerificationUseCase
@Inject
constructor(
    val authRepository: AuthRepository,
    val context: Application
) {
    suspend operator fun invoke(
        phone: String,
        forLogin: Boolean = false,
        forSignup: Boolean = false
    ) =
        flow {
            emit(Resource.Loading(true))

            val result = authRepository.sendAuthVerification(
                phone = phone,
                forLogin = forLogin,
                forSignup = forSignup
            )

            when (result) {
                is ApiResult.Success -> {
                    emit(Resource.Success(result.value))
                }

                is ApiResult.GenericError -> {
                    val message = when (result.code) {
                        NetworkErrors.ERROR_404 -> {
                            context.getString(R.string.user_with_number_was_not_found)
                        }

                        NetworkErrors.ERROR_403 -> {
                            context.getString(R.string.try_again_after_some_time)
                        }

                        NetworkErrors.ERROR_400 -> {
                            if (forSignup)
                                context.getString(R.string.user_with_this_number_already_exists)
                            else
                                context.getString(R.string.bad_request)
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