package com.example.ibrasaloonapp.network.services


import com.example.ibrasaloonapp.network.responses.ServicesResponse
import com.example.ibrasaloonapp.network.responses.WorkersResponse
import com.example.ibrasaloonapp.network.responses.WorkingDateResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkerService {


    @GET("workers")
    suspend fun getWorkers(): WorkersResponse


    @GET("workers/services/{workerId}")
    suspend fun getWorkerServices(@Path("workerId") workerId: String): ServicesResponse


    @GET("workers/working-dates")
    suspend fun getWorkingDates(
        @Query("workerId") workerId: String,
        @Query("fromDate") fromDate: String
    ): WorkingDateResponse


}