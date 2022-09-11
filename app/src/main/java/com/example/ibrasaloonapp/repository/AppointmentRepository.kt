package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.BookAppointmentDto
import com.example.ibrasaloonapp.network.model.UserDto

interface AppointmentRepository {
    suspend fun bookAppointment(
        appointment: BookAppointmentDto
    ): ApiResult<Appointment>

    suspend fun unbookAppointment(id: String): ApiResult<String>

    suspend fun getAvailableAppointments(
        workingDateId: String,
        workerId: String
    ): ApiResult<List<Appointment>>

    suspend fun getAppointment(): ApiResult<Appointment?>

    suspend fun getAppointments(): ApiResult<List<Appointment>>

}

