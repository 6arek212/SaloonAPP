package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.model.UserUpdateDto

interface UserRepository {

    suspend fun getUsers(data: LoginDataDto): ApiResult<List<User>>
    suspend fun getUser(): ApiResult<User>
    suspend fun updateUser(user: UserUpdateDto): ApiResult<User>
    suspend fun deleteUser(): ApiResult<String>


}