package com.example.ibrasaloonapp.repository

import android.util.Log
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.model.UserDtoMapper
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.network.services.UserService
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


private const val TAG = "UserRepositoryImpl"

class UserRepositoryImpl
@Inject
constructor(
    private val userDtoMapper: UserDtoMapper,
    private val userService: UserService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun uploadImage(
        inputStream: InputStream,
        fileType: String
    ): ApiResult<String> {
        return safeApiCall(dispatcher = dispatcher) {
            val part = MultipartBody.Part.createFormData(
                "image",
                "myPic.jpg",
                inputStream.readBytes()
                    .toRequestBody(contentType = "image/${fileType}".toMediaTypeOrNull())
            )
            userService.uploadImage(image = part).fileName
        }
    }

    override suspend fun getUsers(data: LoginDataDto): ApiResult<List<User>> {
        return safeApiCall(dispatcher) {
            userDtoMapper.toDomainList(userService.getUsers().users)
        }
    }

    override suspend fun getUser(): ApiResult<User> {
        return safeApiCall(dispatcher) {
            userDtoMapper.mapToDomainModel(userService.getUser().user)
        }
    }

    override suspend fun updateUser(user: UserUpdateDto): ApiResult<User> {
        return safeApiCall(dispatcher) {
            userDtoMapper.mapToDomainModel(userService.updateUser(user).user)
        }
    }

    override suspend fun deleteUser(): ApiResult<String> {
        return safeApiCall(dispatcher) {
            userService.deleteUser().message
        }
    }
}