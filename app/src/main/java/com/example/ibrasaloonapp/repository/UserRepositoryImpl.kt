package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.model.UserDtoMapper
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.network.services.UserService
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userDtoMapper: UserDtoMapper,
    private val userService: UserService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

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