package com.example.ibrasaloonapp.di

import com.example.ibrasaloonapp.network.services.AppointmentService
import com.example.ibrasaloonapp.network.services.AuthService
import com.example.ibrasaloonapp.network.services.UserService
import com.example.ibrasaloonapp.network.services.WorkerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideAppointmentService(
        retrofit: Provider<Retrofit>
    ) = lazy { retrofit.get().create(AppointmentService::class.java) }.value


    @Singleton
    @Provides
    fun provideAuthService(
        retrofit: Provider<Retrofit>
    ) = lazy { retrofit.get().create(AuthService::class.java) }.value


    @Singleton
    @Provides
    fun provideWorkerService(
        retrofit: Provider<Retrofit>
    ) = lazy { retrofit.get().create(WorkerService::class.java) }.value


    @Singleton
    @Provides
    fun provideUserService(
        retrofit: Provider<Retrofit>
    ) = lazy { retrofit.get().create(UserService::class.java) }.value


}