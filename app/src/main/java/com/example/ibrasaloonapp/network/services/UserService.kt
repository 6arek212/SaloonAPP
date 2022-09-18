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


    @GET("users/{:userId}")
    suspend fun getUser(@Query("userId") userId: String): UserResponse


    @PATCH("users/{:userId}")
    suspend fun updateUser(@Body user: UserUpdateDto, @Query("userId") userId: String): UserResponse


    @DELETE("users/{:userId}")
    suspend fun deleteUser(): MessageResponse


    @Multipart
    @POST("/api/users/upload-image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
    ): FileUploadResponse

}