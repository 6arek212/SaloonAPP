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
    private val appointmentsService: AppointmentService,
    private val mapper: AppointmentDtoMapper,
    private val userMapper: UserDtoMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : AppointmentRepository {


    override suspend fun deleteAppointment(appointmentId: String): ApiResult<String> {
        return safeApiCall(dispatcher) {
            appointmentsService.deleteAppointment(appointmentId = appointmentId).message
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
                appointmentsService.getWorkerAppointments(
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
                appointmentsService.createAppointment(
                    data = appointmentData
                ).appointment
            )
        }
    }


    override suspend fun createRangeAppointments(appointmentsData: CreateAppointmentDto): ApiResult<String> {
        return safeApiCall(dispatcher) {
            appointmentsService.createRangeAppointments(
                data = appointmentsData
            ).message
        }
    }

    override suspend fun updateAppointmentStatus(
        id: String,
        status: String,
        service: String?
    ): ApiResult<Appointment> {
        return safeApiCall(dispatcher) {
            mapper.mapToDomainModel(
                appointmentsService.updateAppointmentStatus(
                    UpdateAppointmentStatusDto(
                        appointmentId = id,
                        status = status,
                        service = service
                    )
                ).appointment
            )
        }
    }

    override suspend fun getAppointments(): ApiResult<List<Appointment>> {
        return safeApiCall(dispatcher) {
            mapper.toDomainList(appointmentsService.getUserAppointments().appointments)
        }
    }

    override suspend fun bookAppointment(
        appointment: BookAppointmentDto
    ): ApiResult<Appointment> {
        return safeApiCall(dispatcher = dispatcher) {
            mapper.mapToDomainModel(
                appointmentsService.bookAppointment(
                    data = appointment
                ).appointment
            )
        }
    }

    override suspend fun unbookAppointment(id: String): ApiResult<String> {
        return safeApiCall(dispatcher = dispatcher) {
            appointmentsService.unbookAppointment(UnBookDto(id)).message
        }
    }

    override suspend fun getAppointment(): ApiResult<Appointment?> {
        return safeApiCall(dispatcher = dispatcher) {
            appointmentsService.getAppointment().appointment?.let {
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
                appointmentsService.getAvailableAppointments(
                    workingDate = workingDate,
                    fromDate = fromDate,
                    workerId = workerId
                ).availableAppointments
            )
        }
    }
}


