package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.model.AuthDataDto
import com.example.ibrasaloonapp.network.model.LoginDataDto
import com.example.ibrasaloonapp.network.responses.WorkersResponse
import com.example.ibrasaloonapp.network.responses.WorkingDateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WorkerService {


    @GET("workers")
    suspend fun getWorkers(): WorkersResponse


    @GET("workers/working-date")
    suspend fun getWorkingDates(
        @Query("workerId") workerId: String,
        @Query("fromDate") fromDate: String
    ): WorkingDateResponse


}