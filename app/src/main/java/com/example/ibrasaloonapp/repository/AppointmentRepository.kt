package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.AppointmentDto

interface AppointmentRepository {

    suspend fun getAppointments(): ApiResult<List<Appointment>>
    suspend fun bookAppointment(
        appointment: AppointmentDto
    ): ApiResult<String>

    suspend fun updateAppointment()
    suspend fun deleteAppointment(id: String): ApiResult<String>
    suspend fun getServiceType(): ApiResult<List<String>>
    suspend fun getAvailableAppointments(date: String): ApiResult<List<String>>

}

