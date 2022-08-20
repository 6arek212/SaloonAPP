package com.example.ibrasaloonapp.di

import android.util.Log
import com.example.ibrasaloonapp.network.model.SessionDtoMapper
import com.example.ibrasaloonapp.network.services.SessionListService
import com.example.ibrasaloonapp.repository.SessionRepository
import com.example.ibrasaloonapp.repository.SessionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideSessionMapper(): SessionDtoMapper {
        return SessionDtoMapper()
    }


    @Singleton
    @Provides
    fun provideSessionRepository(
        service: SessionListService,
        sessionDtoMapper: SessionDtoMapper
    ): SessionRepository {
        Log.d("Aaa", "provideSessionRepository: ")
        return SessionRepositoryImpl(service = service, mapper = sessionDtoMapper)
    }


}