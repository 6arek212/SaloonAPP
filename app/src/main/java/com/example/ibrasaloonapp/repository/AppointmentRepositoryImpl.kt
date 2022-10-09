package com.example.ibrasaloonapp.repository

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
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : AppointmentRepository {


    override suspend fun deleteAppointment(appointmentId: String): ApiResult<String> {
        return safeApiCall(dispatcher) {
            service.deleteAppointment(appointmentId = appointmentId).message
        }
    }

    override suspend fun getWorkerAppointments(
        search: String?,
        status: String?,
        startTime: String?,
        endTime: String?,
        pageSize: Int?,
        currentPage: Int?,
        workerId: String?
    ): ApiResult<List<Appointment>> {
        return safeApiCall(dispatcher) {
            mapper.toDomainList(
                service.getWorkerAppointments(
                    search,
                    startTime,
                    endTime,
                    pageSize,
                    currentPage,
                    workerId,
                    status = status
                ).appointments
            )
        }
    }


    override suspend fun createAppointment(appointmentData: CreateAppointmentDto): ApiResult<Appointment> {
        return safeApiCall(dispatcher) {
            mapper.mapToDomainModel(
                service.createAppointment(
                    data = appointmentData
                ).appointment
            )
        }
    }

    override suspend fun updateAppointmentStatus(
        id: String,
        status: String
    ): ApiResult<Appointment> {
        return safeApiCall(dispatcher) {
            mapper.mapToDomainModel(
                service.updateAppointmentStatus(
                    UpdateAppointmentStatusDto(
                        appointmentId = id,
                        status = status
                    )
                ).appointment
            )
        }
    }

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
        fromDate: String,
        workerId: String
    ): ApiResult<List<Appointment>> {
        return safeApiCall(dispatcher = dispatcher) {
            mapper.toDomainList(
                service.getAvailableAppointments(
                    workingDate = workingDate,
                    fromDate = fromDate,
                    workerId = workerId
                ).availableAppointments
            )
        }
    }
}


