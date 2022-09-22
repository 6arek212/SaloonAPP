package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.trainingapp.network.NetworkErrors
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "GetAvailableAppointment"

class GetAvailableAppointmentUseCase
@Inject
constructor(
    private val context: Application,
    private val appointmentsRepository: AppointmentRepository,
) {


    suspend operator fun invoke(workingDate: String, fromDate: String, workerId: String) = flow {
        emit(Resource.Loading(true))

        val result = appointmentsRepository.getAvailableAppointments(
            workingDate,
            fromDate,
            workerId
        )

        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "getAvailableAppointments: ${result.value}")
                emit(Resource.Success(data = result.value))
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