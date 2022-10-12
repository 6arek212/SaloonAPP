package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import java.io.InputStream

interface UserRepository {
    suspend fun getUsers(
        search: String? = null,
        sort: Int? = null,
        pageSize: Int? = null,
        currentPage: Int? = null,
    ): ApiResult<Triple<List<User>, Int, Int>>

    suspend fun getUser(userId: String): ApiResult<Triple<User, Int, Double>>
    suspend fun updateUser(userDto: UserUpdateDto, userId: String): ApiResult<User>
    suspend fun deleteUser(userId: String): ApiResult<String>
    suspend fun uploadImage(inputStream: InputStream, fileType: String): ApiResult<String>
}