package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.*
import com.example.ibrasaloonapp.network.responses.*
import retrofit2.http.*

interface AppointmentService {

    @GET("appointments")
    suspend fun getWorkerAppointments(
        @Query("search") search: String? = null,
        @Query("start_time") startTime: String? = null,
        @Query("end_time") endTime: String? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("currentPage") currentPage: Int? = null,
        @Query("workerId") workerId: String? = null,
        @Query("status") status: String? = null
    ): AppointmentsUserListResponse


    @PATCH("appointments/update-status")
    suspend fun updateAppointmentStatus(@Body data: UpdateAppointmentStatusDto): UpdateAppointmentResponse


    @POST("appointments")
    suspend fun createAppointment(@Body data: CreateAppointmentDto): UpdateAppointmentResponse

    @DELETE("appointments/{appointmentId}")
    suspend fun deleteAppointment(@Path("appointmentId") appointmentId: String): MessageResponse

    @GET("appointments/user-appointments")
    suspend fun getUserAppointments(): AppointmentsUserListResponse

    @GET("appointments/user-appointment")
    suspend fun getAppointment(): AppointmentResponse

    @POST("appointments/book")
    suspend fun bookAppointment(
        @Body data: BookAppointmentDto
    ): AppointmentNotNulResponse

    @POST("appointments/unbook")
    suspend fun unbookAppointment(
        @Body dto: UnBookDto
    ): MessageResponse


    @GET("appointments/available")
    suspend fun getAvailableAppointments(
        @Query("workingDate") workingDate: String,
        @Query("fromDate") fromDate: String,
        @Query("workerId") workerId: String
    ): AvailableAppointmentsResponse


}