package com.example.ibrasaloonapp.di

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
import javax.inject.Singleton

const val BASE_API = "http://192.168.1.46:4000/api/"

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
    fun provideRetrofit(
        authInterceptor: AuthInterceptor
    ): Retrofit {


        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)


        return Retrofit.Builder()
            .baseUrl(BASE_API)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }


}