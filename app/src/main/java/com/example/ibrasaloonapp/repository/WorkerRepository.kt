package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Service
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.AddServiceDto

interface WorkerRepository {

    suspend fun getWorkers(): ApiResult<List<User>>

    suspend fun getWorkerServices(workerId: String): ApiResult<List<Service>>

    suspend fun addService(data: AddServiceDto): ApiResult<Service>

    suspend fun deleteService(serviceId: String): ApiResult<String>

    suspend fun getWorkingDates(workerId: String, fromDate: String): ApiResult<List<String>>

}