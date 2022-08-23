package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.AppointmentDto
import com.example.ibrasaloonapp.network.responses.AppointmentListResponse
import com.example.ibrasaloonapp.network.responses.AvailableAppointmentsResponse
import com.example.ibrasaloonapp.network.responses.BookAppointmentResponse
import com.example.ibrasaloonapp.network.responses.MessageResponse
import retrofit2.http.*

interface AppointmentService {

    @GET("appointments")
    suspend fun getAppointments(): AppointmentListResponse

    @POST("appointments")
    suspend fun bookAppointment(
        @Body data: AppointmentDto
    ): BookAppointmentResponse

    @PATCH("appointments/{id}")
    suspend fun updateAppointment(
        @Path("id") id: String,
        @Body updateOps: HashMap<String, Any>
    )

    @DELETE("appointments/{id}")
    suspend fun deleteAppointment(
        @Path("id") id: String
    ): MessageResponse


    @GET("appointments/availableAppointment")
    suspend fun getAvailableAppointments(@Query("date") date: String): AvailableAppointmentsResponse

}