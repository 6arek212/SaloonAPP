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
    suspend fun getUsers(
        @Query("search") search: String? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("currentPage") currentPage: Int? = null,
        @Query("sort") sort: Int? = null
    ): UsersResponse


    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): UserResponse


    @PATCH("users/{userId}")
    suspend fun updateUser(@Path("userId") userId: String, @Body user: UserUpdateDto): UserResponse


    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: String): MessageResponse


    @Multipart
    @POST("/api/users/upload-image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
    ): FileUploadResponse

}