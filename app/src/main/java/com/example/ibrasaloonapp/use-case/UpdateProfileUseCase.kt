package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.presentation.ui.edit_profile.EditProfileViewModel
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.UserRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "UpdateProfileUseCase"

class UpdateProfileUseCase
@Inject
constructor(
    private val context: Application,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {


    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String,
        userId: String
    ) = flow {
        emit(Resource.Loading(true))


        val userUpdate =
            UserUpdateDto(firstName = firstName, lastName = lastName, phone = phone)


        val result = userRepository.updateUser(userDto = userUpdate, userId = userId)
        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "updateProfile: updated")
                emit(Resource.Success(data = result.value))
            }
            is ApiResult.GenericError -> {
                val message = result.code.defaultErrorMessage(context = context)
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