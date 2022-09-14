package com.example.ibrasaloonapp.di

import android.app.Application
import com.example.ibrasaloonapp.core.Logger
import com.example.ibrasaloonapp.network.utils.ConnectivityObserver
import com.example.ibrasaloonapp.network.utils.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ConnectivityManagerModule {


    @Singleton
    @Provides
    fun provideConnectivityManager(context: Application): ConnectivityObserver {
        return NetworkConnectivityObserver(context = context)
    }


}