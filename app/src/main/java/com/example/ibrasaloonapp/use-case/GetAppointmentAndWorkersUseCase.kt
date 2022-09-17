package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.WorkerRepository
import com.example.ibrasaloonapp.ui.CustomString
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.trainingapp.network.NetworkErrors
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "GetWorkersUseCase"

class GetAppointmentAndWorkersUseCase
@Inject
constructor(
    private val context: Application,
    private val authRepository: AuthRepository,
    private val workerRepository: WorkerRepository,
    private val appointmentRepository: AppointmentRepository,
    private val userId: CustomString
) {

    class WorkersListWrapper(val workers: List<User>)


    suspend operator fun invoke() = flow {

        emit(Resource.Loading(true))
        if (userId.value != null) {

            when (val appointmentResult = appointmentRepository.getAppointment()) {

                is ApiResult.Success -> {
                    Log.d(TAG, "getAppointment: ${appointmentResult.value}")
                    emit(Resource.Success(appointmentResult.value))
                }

                is ApiResult.GenericError -> {
                    val message = appointmentResult.code.defaultErrorMessage(context = context)
                    if (appointmentResult.code == 401) {
                        authRepository.logout()
                    }
                    emit(Resource.Error(message = message))
                }

                is ApiResult.NetworkError -> {
                    emit(Resource.Error(message = context.getString(R.string.something_went_wrong)))
                }
            }
        }


        when (val workersResult = workerRepository.getWorkers()) {
            is ApiResult.Success -> {
                Log.d(TAG, "getWorkers: ${workersResult.value}")
                emit(Resource.Success(WorkersListWrapper(workersResult.value)))
            }

            is ApiResult.GenericError -> {
                Log.d(TAG, "GenericError: ${workersResult.errorMessage}")
                val message = workersResult.code.defaultErrorMessage(context = context)

                if (workersResult.code == 401) {
                    authRepository
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