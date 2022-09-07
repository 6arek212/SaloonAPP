package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto

interface UserRepository {

    suspend fun getUsers(data: LoginDataDto): ApiResult<List<User>>
    suspend fun getUser(): ApiResult<User>
    suspend fun updateUser(user: User): ApiResult<String>
    suspend fun deleteUser(): ApiResult<String>


}