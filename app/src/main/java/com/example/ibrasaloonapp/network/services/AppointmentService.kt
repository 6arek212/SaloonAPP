package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.AppointmentDto
import com.example.ibrasaloonapp.network.model.BookAppointmentDto
import com.example.ibrasaloonapp.network.responses.*
import retrofit2.http.*

interface AppointmentService {

    @GET("appointments/user-appointments")
    suspend fun getUserAppointments(): AppointmentsUserListResponse

    @GET("appointments/user-appointment")
    suspend fun getAppointment(): AppointmentResponse

    @POST("appointments/book")
    suspend fun bookAppointment(
        @Body data: BookAppointmentDto
    ): MessageResponse

    @POST("appointments/unbook")
    suspend fun unbookAppointment(
        @Body appointmentId: String
    ): MessageResponse


    @GET("appointments/available")
    suspend fun getAvailableAppointments(
        @Query("date") date: String,
        @Query("worker") workerId: String
    ): AvailableAppointmentsResponse


}