package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.core.ServiceType
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.*
import com.example.ibrasaloonapp.network.services.AppointmentService
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

private const val TAG = "AppointmentRepositoryIm"

class AppointmentRepositoryImpl
@Inject
constructor(
    private val service: AppointmentService,
    private val mapper: AppointmentDtoMapper,
    private val userMapper: UserDtoMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppointmentRepository {

    override suspend fun getAppointments(): ApiResult<List<Appointment>> {
        return safeApiCall(dispatcher) {
            mapper.toDomainList(service.getUserAppointments().appointments)
        }
    }

    override suspend fun bookAppointment(
        appointment: BookAppointmentDto
    ): ApiResult<Appointment> {
        return safeApiCall(dispatcher = dispatcher) {
            mapper.mapToDomainModel(
                service.bookAppointment(
                    data = appointment
                ).appointment
            )
        }
    }

    override suspend fun unbookAppointment(id: String): ApiResult<String> {
        return safeApiCall(dispatcher = dispatcher) {
            service.unbookAppointment(UnBookDto(id)).message
        }
    }

    override suspend fun getAppointment(): ApiResult<Appointment?> {
        return safeApiCall(dispatcher = dispatcher) {
            service.getAppointment().appointment?.let {
                mapper.mapToDomainModel(it)
            }
        }
    }


    override suspend fun getAvailableAppointments(
        workingDate: String,
        workerId: String
    ): ApiResult<List<Appointment>> {
        return safeApiCall(dispatcher = dispatcher) {
            mapper.toDomainList(
                service.getAvailableAppointments(
                    workingDate = workingDate,
                    workerId = workerId
                ).availableAppointments
            )
        }
    }
}


val fakeAvailableDates = listOf(
    "12/3/2022",
    "30/5/2022",
    "6/6/2022",
    "5/7/2022",
    "3/8/2022",
)


val fakeServiceType = ServiceType.values()