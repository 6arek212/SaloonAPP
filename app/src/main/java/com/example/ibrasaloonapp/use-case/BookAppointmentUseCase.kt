package com.example.ibrasaloonapp.use

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.BookAppointmentDto
import com.example.ibrasaloonapp.presentation.ui.book_appointment.BookAppointmentUIEvent
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.trainingapp.network.NetworkErrors
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "BookAppointmentUseCase"

class BookAppointmentUseCase
@Inject
constructor(
    val appointmentRepository: AppointmentRepository,
    val context: Application
) {

    suspend operator fun invoke(service: String, appointmentId: String, userId: String) = flow {

        emit(Resource.Loading(true))

        val appointmentDTO =
            BookAppointmentDto(
                service = service,
                appointmentId = appointmentId,
                userId = userId
            )

        val result = appointmentRepository.bookAppointment(appointmentDTO)

        when (result) {
            is ApiResult.Success -> {
                emit(Resource.Success(data = result.value))
            }
            is ApiResult.GenericError -> {
                Log.d(TAG, "GenericError: ${result.errorMessage}")
                val message = when (result.code) {
                    NetworkErrors.ERROR_400 -> {
                        when (result.genericCode) {
                            1 -> context.getString(R.string.you_already_have_an_appointment)
                            2 -> context.getString(R.string.appointment_has_already_been_booked)
                            else -> context.getString(R.string.bad_request)
                        }
                    }

                    NetworkErrors.ERROR_401 -> {
                        context.getString(R.string.not_authorized)
                    }

                    else -> {
                        context.getString(R.string.something_went_wrong)
                    }
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