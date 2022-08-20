package com.example.ibrasaloonapp.di

import com.example.ibrasaloonapp.network.services.SessionListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideSessionListService(
        retrofit: Retrofit
    ) = lazy { retrofit.create(SessionListService::class.java) }.value


}