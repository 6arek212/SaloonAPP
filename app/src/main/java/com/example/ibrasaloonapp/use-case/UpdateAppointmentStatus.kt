package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.WorkerRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.trainingapp.network.NetworkErrors
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "GetAppointmentsUseCase"

class UpdateAppointmentStatus
@Inject
constructor(
    private val context: Application,
    private val appointmentsRepository: AppointmentRepository
) {


    suspend operator fun invoke(appointmentId: String, status: String, service: String? = null) =
        flow {


            emit(Resource.Loading(true))

            val result =
                appointmentsRepository.updateAppointmentStatus(
                    id = appointmentId,
                    status = status,
                    service = service
                )

            when (result) {
                is ApiResult.Success -> {
                    emit(Resource.Success(data = result.value))
                }

                is ApiResult.GenericError -> {
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