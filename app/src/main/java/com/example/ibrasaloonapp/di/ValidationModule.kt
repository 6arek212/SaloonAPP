package com.example.ibrasaloonapp.di

import com.example.ibrasaloonapp.domain.use_case.ValidatePassword
import com.example.ibrasaloonapp.domain.use_case.ValidatePhoneNumber
import com.example.ibrasaloonapp.domain.use_case.ValidateRequired
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ValidationModule {


    @Provides
    @Singleton
    fun provideValidationRequired(): ValidateRequired {
        return ValidateRequired()
    }

    @Provides
    @Singleton
    fun providePhoneValidation(): ValidatePhoneNumber {
        return ValidatePhoneNumber()
    }

    @Provides
    @Singleton
    fun providePasswordValidation(): ValidatePassword {
        return ValidatePassword()
    }

}