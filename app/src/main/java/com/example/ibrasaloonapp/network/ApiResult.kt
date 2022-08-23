package com.example.ibrasaloonapp.network

sealed class ApiResult<out T> {

    data class Success<out T>(val value: T): ApiResult<T>()

    data class GenericError(
        val code: Int? = null,
        val errorMessage: String = ""
    ): ApiResult<Nothing>()

    object NetworkError: ApiResult<Nothing>()
}
