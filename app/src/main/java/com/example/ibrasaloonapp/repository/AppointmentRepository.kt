package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.AppointmentDto

interface AppointmentRepository {
    suspend fun bookAppointment(
        appointment: AppointmentDto
    ): ApiResult<String>

    suspend fun unbookAppointment(id: String): ApiResult<String>

    suspend fun getAvailableAppointments(
        date: String,
        workerId: String
    ): ApiResult<List<Appointment>>

    suspend fun getAppointment(): ApiResult<Appointment>
}

