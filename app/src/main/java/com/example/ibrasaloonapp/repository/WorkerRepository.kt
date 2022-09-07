package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.domain.model.WorkingDate
import com.example.ibrasaloonapp.network.ApiResult

interface WorkerRepository {

    suspend fun getWorkers(): ApiResult<List<User>>

    suspend fun getWorkingDates(workerId: String, fromDate: String): ApiResult<List<WorkingDate>>

}