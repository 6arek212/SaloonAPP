package com.example.ibrasaloonapp.di

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.ibrasaloonapp.network.model.AppointmentDtoMapper
import com.example.ibrasaloonapp.network.model.AuthDataDtoMapper
import com.example.ibrasaloonapp.network.model.CustomerDtoMapper
import com.example.ibrasaloonapp.network.services.AppointmentService
import com.example.ibrasaloonapp.network.services.AuthService
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.repository.AppointmetRepositoryImpl
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAppointmentMapper(customerMapper: CustomerDtoMapper): AppointmentDtoMapper {
        return AppointmentDtoMapper(customerMapper)
    }

    @Singleton
    @Provides
    fun provideCustomerMapper(): CustomerDtoMapper {
        return CustomerDtoMapper()
    }

    @Singleton
    @Provides
    fun provideAuthDataMapper(customerMapper: CustomerDtoMapper): AuthDataDtoMapper {
        return AuthDataDtoMapper(customerMapper)
    }


    @Singleton
    @Provides
    fun provideAppointmentRepository(
        service: AppointmentService,
        appointmentDtoMapper: AppointmentDtoMapper
    ): AppointmentRepository {
        return AppointmetRepositoryImpl(service = service, mapper = appointmentDtoMapper)
    }


    @Singleton
    @Provides
    fun provideAuthRepository(
        service: AuthService,
        mapper: AuthDataDtoMapper,
        @ApplicationContext
        application: Context
    ): AuthRepository {
        return AuthRepositoryImpl(authService = service, authDataDtoMapper = mapper, application = application)
    }

}