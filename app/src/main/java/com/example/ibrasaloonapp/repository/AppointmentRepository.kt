package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.BookAppointmentDto
import com.example.ibrasaloonapp.network.model.CreateAppointmentDto
import com.example.ibrasaloonapp.network.model.UserDto
import retrofit2.http.Query

interface AppointmentRepository {
    suspend fun bookAppointment(
        appointment: BookAppointmentDto
    ): ApiResult<Appointment>

    suspend fun unbookAppointment(id: String): ApiResult<String>

    suspend fun getAvailableAppointments(
        workingDate: String,
        fromDate: String,
        workerId: String
    ): ApiResult<List<Appointment>>

    suspend fun getAppointment(): ApiResult<Appointment?>

    suspend fun getAppointments(): ApiResult<List<Appointment>>

    suspend fun getWorkerAppointments(
        search: String? = null,
        status: String? = null,
        startTime: String? = null,
        endTime: String? = null,
        pageSize: Int? = null,
        currentPage: Int? = null,
        workerId: String? = null
    ): ApiResult<List<Appointment>>


    suspend fun updateAppointmentStatus(
        id: String,
        status: String,
        service: String? = null
    ): ApiResult<Appointment>

    suspend fun createAppointment(appointmentData: CreateAppointmentDto): ApiResult<Appointment>

    suspend fun createRangeAppointments(appointmentsData: CreateAppointmentDto): ApiResult<String>

    suspend fun deleteAppointment(appointmentId: String): ApiResult<String>

}

