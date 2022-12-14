package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.KeyValueWrapper
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.AddServiceDto
import com.example.ibrasaloonapp.network.services.WorkerService
import com.example.ibrasaloonapp.repository.WorkerRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.trainingapp.network.NetworkErrors.ERROR_400
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "GetServicesUseCase"


class AddServiceUseCase
@Inject
constructor(
    private val workerRepository: WorkerRepository,
    val context: Application
) {

    suspend operator fun invoke(workerId: String, title: String, price: Double) = flow {
        emit(Resource.Loading(true))


        val result = workerRepository.addService(
            data = AddServiceDto(
                workerId = workerId,
                title = title,
                price = price
            )
        )

        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, " ${result.value}")
                emit(Resource.Success(result.value))
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, " ${result.errorMessage}")
                var message = result.code.defaultErrorMessage(context = context)
                if (result.code == ERROR_400){
                    message = context.getString(R.string.you_already_have_this_service)
                }
                emit(Resource.Error(message = message))
            }

            is ApiResult.NetworkError -> {
                emit(Resource.Error(message = context.getString(R.string.something_went_wrong)))
            }
        }

        emit(Resource.Loading(false))
    }


}