package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Service
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.AddServiceDto
import com.example.ibrasaloonapp.network.model.ServiceDtoMapper
import com.example.ibrasaloonapp.network.model.UserDtoMapper
import com.example.ibrasaloonapp.network.services.WorkerService
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class WorkerRepositoryImpl
@Inject
constructor(
    private val userDtoMapper: UserDtoMapper,
    private val serviceDtoMapper: ServiceDtoMapper,
    private val workerService: WorkerService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : WorkerRepository {

    override suspend fun getWorkers(): ApiResult<List<User>> {
        return safeApiCall(dispatcher) {
            userDtoMapper.toDomainList(workerService.getWorkers().workers)
        }
    }

    override suspend fun addService(data: AddServiceDto): ApiResult<Service> {
        return safeApiCall(dispatcher) {
            serviceDtoMapper.mapToDomainModel(
                workerService.addService(
                    data = data
                ).service
            )
        }
    }

    override suspend fun deleteService(serviceId: String): ApiResult<String> {
        return safeApiCall(dispatcher) {
            workerService.deleteService(
                serviceId = serviceId
            ).message
        }
    }

    override suspend fun getWorkerServices(workerId: String): ApiResult<List<Service>> {
        return safeApiCall(dispatcher) {
            serviceDtoMapper.toDomainList(
                workerService.getWorkerServices(
                    workerId = workerId
                ).services
            )
        }
    }

    override suspend fun getWorkingDates(
        workerId: String,
        fromDate: String
    ): ApiResult<List<String>> {
        return safeApiCall(dispatcher) {
            workerService.getWorkingDates(
                workerId,
                fromDate
            ).workingDates
        }
    }
}