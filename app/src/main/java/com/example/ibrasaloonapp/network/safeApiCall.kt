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
//            withTimeout(NETWORK_TIMEOUT) {
                ApiResult.Success(apiCall.invoke())
//            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code = code, errorMessage = NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val (message, errorCode) = convertErrorBody(throwable)
                    Log.d(TAG, "code: ${code} HttpException: ${message} errorCode: ${errorCode}")
                    ApiResult.GenericError(
                        code = code,
                        genericCode = errorCode,
                        errorMessage = message ?: ""
                    )
                }
                else -> {
                    Log.d(TAG, "safeApiCall: $NETWORK_ERROR_UNKNOWN")
                    ApiResult.GenericError(
                        code = null,
                        errorMessage = NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}


private fun convertErrorBody(throwable: HttpException): Pair<String?, Int?> {
    return try {
        throwable.response()?.errorBody()?.string()?.let {
            Log.d(TAG, "convertErrorBody: ${it} ${ JSONObject(it).getInt("errorCode")}")
           return Pair(JSONObject(it).getString("message"), JSONObject(it).getInt("errorCode"))
        }
        Pair(ERROR_UNKNOWN, null)
    } catch (exception: Exception) {
        Pair(ERROR_UNKNOWN, null)
    }
}


