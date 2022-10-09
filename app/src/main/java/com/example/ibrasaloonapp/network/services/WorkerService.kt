package com.example.ibrasaloonapp.network.services


import com.example.ibrasaloonapp.network.model.AddServiceDto
import com.example.ibrasaloonapp.network.responses.*
import retrofit2.http.*

interface WorkerService {


    @GET("workers")
    suspend fun getWorkers(): WorkersResponse

    @GET("workers/services/{workerId}")
    suspend fun getWorkerServices(@Path("workerId") workerId: String): ServicesResponse

    @POST("workers/services")
    suspend fun addService(@Body data: AddServiceDto): AddServiceResponse

    @DELETE("workers/services/{serviceId}")
    suspend fun deleteService(@Path("serviceId") serviceId: String): MessageResponse

    @GET("workers/working-dates")
    suspend fun getWorkingDates(
        @Query("workerId") workerId: String,
        @Query("fromDate") fromDate: String
    ): WorkingDateResponse


}