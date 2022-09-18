package com.example.ibrasaloonapp.di

import android.app.Application
import com.example.ibrasaloonapp.domain.use_case.ValidateName
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
    fun provideValidationRequired(context: Application): ValidateRequired {
        return ValidateRequired(context)
    }

    @Provides
    @Singleton
    fun providePhoneValidation(context: Application): ValidatePhoneNumber {
        return ValidatePhoneNumber(context)
    }


    @Provides
    @Singleton
    fun provideNameValidation(context: Application): ValidateName {
        return ValidateName(context)
    }

}