package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.WorkerRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.trainingapp.network.NetworkErrors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "GetWorkersUseCase"

class GetWorkersUseCase
@Inject
constructor(
    private val context: Application,
    private val workerRepository: WorkerRepository,
) {


    suspend operator fun invoke(): Flow<Resource<List<User>>> = flow {

        emit(Resource.Loading(true))

        val result = workerRepository.getWorkers()

        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "getWorkers: ${result.value}")
                emit(Resource.Success(result.value))
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "GenericError: ${result.errorMessage}")
                val message = result.code.defaultErrorMessage(context = context)
                emit(Resource.Error(message = message))
            }

            is ApiResult.NetworkError -> {
                emit(Resource.Error(message = context.getString(R.string.something_went_wrong)))
            }
        }

        emit(Resource.Loading(false))
    }

}