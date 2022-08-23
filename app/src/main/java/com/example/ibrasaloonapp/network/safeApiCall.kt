package com.example.trainingapp.util

import android.util.Log
import com.example.ibrasaloonapp.network.ApiResult
import com.example.trainingapp.network.NetworkConstants.NETWORK_TIMEOUT
import com.example.trainingapp.network.NetworkErrors.ERROR_UNKNOWN
import com.example.trainingapp.network.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.example.trainingapp.network.NetworkErrors.NETWORK_ERROR_UNKNOWN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */
private const val TAG = "safeApiCall"

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ApiResult<T> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT) {
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is Code400Exception -> {
                    ApiResult.GenericError(throwable.code, throwable.message ?: "")
                }

                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }

                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    Log.d(TAG, "HttpException ${errorResponse}")
                    ApiResult.GenericError(
                        code,
                        errorResponse ?: ""
                    )
                }
                else -> {
                    Log.d(TAG, "safeApiCall: $NETWORK_ERROR_UNKNOWN")
                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}


class Code400Exception(message: String? = "SOME KIND OF ERROR 400-499", val code: Int) :
    IOException(message) {
}


private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()?.let {
            JSONObject(it).getString("message")
        }
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}


