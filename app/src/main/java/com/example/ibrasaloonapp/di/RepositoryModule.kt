package com.example.ibrasaloonapp.di

import android.app.Application
import android.content.Context
import com.example.ibrasaloonapp.network.model.AppointmentDtoMapper
import com.example.ibrasaloonapp.network.model.AuthDataDtoMapper
import com.example.ibrasaloonapp.network.model.ServiceDtoMapper
import com.example.ibrasaloonapp.network.model.UserDtoMapper
import com.example.ibrasaloonapp.network.services.AppointmentService
import com.example.ibrasaloonapp.network.services.AuthService
import com.example.ibrasaloonapp.network.services.UserService
import com.example.ibrasaloonapp.network.services.WorkerService
import com.example.ibrasaloonapp.repository.*
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
    fun provideAppointmentMapper(
        customerMapper: UserDtoMapper,
        serviceDtoMapper: ServiceDtoMapper
    ): AppointmentDtoMapper {
        return AppointmentDtoMapper(
            customerMapper = customerMapper,
            serviceDtoMapper = serviceDtoMapper
        )
    }

    @Singleton
    @Provides
    fun provideServiceMapper(application: Application): ServiceDtoMapper {
        return ServiceDtoMapper(context = application)
    }

    @Singleton
    @Provides
    fun provideUserMapper(): UserDtoMapper {
        return UserDtoMapper()
    }

    @Singleton
    @Provides
    fun provideAuthDataMapper(customerMapper: UserDtoMapper): AuthDataDtoMapper {
        return AuthDataDtoMapper(customerMapper)
    }


    @Singleton
    @Provides
    fun provideAppointmentRepository(
        service: AppointmentService,
        appointmentDtoMapper: AppointmentDtoMapper,
        userMapper: UserDtoMapper
    ): AppointmentRepository {
        return AppointmentRepositoryImpl(
            service = service,
            mapper = appointmentDtoMapper,
            userMapper = userMapper
        )
    }


    @Singleton
    @Provides
    fun provideAuthRepository(
        service: AuthService,
        mapper: AuthDataDtoMapper,
        @ApplicationContext
        application: Context
    ): AuthRepository {
        return AuthRepositoryImpl(
            authService = service,
            authDataDtoMapper = mapper,
            application = application
        )
    }


    @Singleton
    @Provides
    fun provideWorkerRepository(
        userDtoMapper: UserDtoMapper,
        workerService: WorkerService,
        serviceDtoMapper: ServiceDtoMapper
    ): WorkerRepository {
        return WorkerRepositoryImpl(
            userDtoMapper = userDtoMapper,
            workerService = workerService,
            serviceDtoMapper = serviceDtoMapper
        )
    }


    @Singleton
    @Provides
    fun provideUserRepository(
        userDtoMapper: UserDtoMapper,
        userService: UserService,
        authRepository: AuthRepository
    ): UserRepository {
        return UserRepositoryImpl(
            userDtoMapper = userDtoMapper,
            userService = userService,
            authRepository = authRepository
        )
    }

}