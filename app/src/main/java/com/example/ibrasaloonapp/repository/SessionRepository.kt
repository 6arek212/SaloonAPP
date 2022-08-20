package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.Session
import com.example.ibrasaloonapp.network.ApiResult

interface SessionRepository {

    suspend fun getSessions(): ApiResult<List<Session>>
    suspend fun getAvailableAppointments(): ApiResult<List<String>>
    suspend fun getServiceType(): ApiResult<List<String>>

}