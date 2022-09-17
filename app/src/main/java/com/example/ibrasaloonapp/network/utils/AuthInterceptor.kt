package com.example.trainingapp.util


import android.util.Log
import com.example.ibrasaloonapp.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

private const val TAG = "AuthInterceptor"

class AuthInterceptor
@Inject
constructor(
    val auth: Provider<AuthRepository>
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()


        val authData = runBlocking(Dispatchers.IO) {
            auth.get().getCacheAuthData()
        }

        Log.d(TAG, "intercept: ${authData}")

        // If token has been saved, add it to the request
        authData?.let {
            requestBuilder.addHeader("Authorization", "Bearer ${it.token}")
                .header("Accept", "application/json")
        }

        return chain.proceed(requestBuilder.build())
    }

}

