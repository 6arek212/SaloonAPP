package com.example.ibrasaloonapp.di

import android.os.Looper
import android.util.Log
import com.example.ibrasaloonapp.network.utils.TokenAuthenticator
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.trainingapp.util.AuthInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

const val BASE_API = "http://192.168.1.46:4000/api/"
//const val BASE_API = "https://saloon-ibra-api.herokuapp.com/api/"

private const val TAG = "RetrofitModule"
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

//    @Singleton
//    @Provides
//    fun provideAuthInterceptor(
//        authRepository: AuthRepository
//    ): AuthInterceptor = lazy { AuthInterceptor(authRepository) }.value

    @Singleton
    @Provides
    fun provideOkHttp(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient.Builder {

        Log.d(TAG, "provideOkHttp: ${Thread.currentThread()}")
        return OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
    }


    @Singleton
    @Provides
    fun provideRetrofit(
        client: Provider<OkHttpClient.Builder>
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_API)
            .client(client.get().build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }


}