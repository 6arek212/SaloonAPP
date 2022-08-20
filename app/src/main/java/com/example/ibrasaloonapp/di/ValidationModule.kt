package com.example.ibrasaloonapp.di

import com.example.ibrasaloonapp.domain.user_case.ValidateRequired
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ValidationModule {


    @Provides
    @Singleton
    fun provideValidationRequired(): ValidateRequired {
        return ValidateRequired()
    }

}