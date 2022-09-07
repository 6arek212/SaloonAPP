package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.domain.model.WorkingDate
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.UserDtoMapper
import com.example.ibrasaloonapp.network.model.WorkingDateDtoMapper
import com.example.ibrasaloonapp.network.services.WorkerService
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class WorkerRepositoryImpl
@Inject
constructor(
    private val workingDateDtoMapper: WorkingDateDtoMapper,
    private val userDtoMapper: UserDtoMapper,
    private val workerService: WorkerService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WorkerRepository {

    override suspend fun getWorkers(): ApiResult<List<User>> {
        return safeApiCall(dispatcher) {
            userDtoMapper.toDomainList(workerService.getWorkers().workers)
        }
    }

    override suspend fun getWorkingDates(
        workerId: String,
        fromDate: String
    ): ApiResult<List<WorkingDate>> {
        return safeApiCall(dispatcher) {
            workingDateDtoMapper.toDomainList(
                workerService.getWorkingDates(
                    workerId,
                    fromDate
                ).workingDates
            )
        }
    }
}