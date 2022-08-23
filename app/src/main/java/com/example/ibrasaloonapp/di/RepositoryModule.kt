package com.example.ibrasaloonapp.di

import android.util.Log
import com.example.ibrasaloonapp.network.model.AppointmentDtoMapper
import com.example.ibrasaloonapp.network.model.CustomerDtoMapper
import com.example.ibrasaloonapp.network.services.AppointmentService
import com.example.ibrasaloonapp.repository.AppointmentRepository
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
    fun provideSessionRepository(
        service: AppointmentService,
        sessionDtoMapper: AppointmentDtoMapper
    ): AppointmentRepository {
        Log.d("Aaa", "provideSessionRepository: ")
        return SessionRepositoryImpl(service = service, mapper = sessionDtoMapper)
    }


}