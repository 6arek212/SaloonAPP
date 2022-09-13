package com.example.ibrasaloonapp.network.utils

import android.util.Log
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider


private const val TAG = "TokenAuthenticator"

class TokenAuthenticator
@Inject
constructor(
    val auth: Provider<AuthRepository>
) : okhttp3.Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        Log.d(TAG, "authenticate: start authenticating")
        val token = getCurrentToken()

        synchronized(this) {
            val newToken = getCurrentToken()

            if (response.request.header("Authorization") != null) {
                Log.d(TAG, "authenticate:  new Token : ${newToken}")
                if (newToken != token) {
                    return response
                        .request
                        .newBuilder()
                        .removeHeader("Authorization")
                        .header("Authorization", "Bearer $newToken")
                        .build()
                }


                val updatedToken = getNewToken()
                if (updatedToken != null)
                    return response.request
                        .newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer $updatedToken")
                        .build()
            }

            return null
        }
    }

    private fun getCurrentToken(): String? {
        return runBlocking(Dispatchers.IO) {
            auth.get().getLoginStatus()?.token
        }
    }

    private fun getNewToken(): String? {
        return runBlocking(Dispatchers.IO) {
            auth.get().refreshToken()
        }
    }

}