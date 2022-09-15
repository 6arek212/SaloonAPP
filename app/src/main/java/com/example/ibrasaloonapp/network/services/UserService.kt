package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.network.responses.FileUploadResponse
import com.example.ibrasaloonapp.network.responses.MessageResponse
import com.example.ibrasaloonapp.network.responses.UserResponse
import com.example.ibrasaloonapp.network.responses.UsersResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface UserService {

    @GET("users")
    suspend fun getUsers(): UsersResponse


    @GET("users/{:id}")
    suspend fun getUser(): UserResponse


    @PATCH("users")
    suspend fun updateUser(@Body user: UserUpdateDto): UserResponse


    @DELETE("users/{:id}")
    suspend fun deleteUser(): MessageResponse


    @Multipart
    @POST("/api/users/upload-image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
    ): FileUploadResponse

}