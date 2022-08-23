package com.example.ibrasaloonapp.di

import com.example.ibrasaloonapp.network.services.AppointmentService
import com.example.ibrasaloonapp.network.services.AuthService
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
    fun provideAppointmentService(
        retrofit: Retrofit
    ) = lazy { retrofit.create(AppointmentService::class.java) }.value


    @Singleton
    @Provides
    fun provideAuthService(
        retrofit: Retrofit
    ) = lazy { retrofit.create(AuthService::class.java) }.value

}