package com.example.ibrasaloonapp.use

import android.app.Application
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.ui.upload.UploadUIEvent
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.UserRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import javax.inject.Inject


private const val TAG = "UploadImageUseCase"

class UploadImageUseCase
@Inject
constructor(
    private val context: Application,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,

    ) {

    suspend operator fun invoke(inputStream: InputStream, fileType: String) = flow {

        emit(Resource.Loading(true))


        val result = userRepository.uploadImage(inputStream, fileType)

        when (result) {
            is ApiResult.Success -> {
                authRepository.updateUserImage(path = result.value)
                emit(Resource.Success(data = result.value))
            }
            is ApiResult.GenericError -> {
                val message = result.code.defaultErrorMessage(context = context)
                emit(Resource.Error(message = message))
            }

            is ApiResult.NetworkError -> {
                emit(Resource.Error(message = context.getString(R.string.something_went_wrong)))
            }
        }



        emit(Resource.Loading(false))
    }


}