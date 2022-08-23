package com.example.ibrasaloonapp.repository

import androidx.compose.ui.text.capitalize
import com.example.ibrasaloonapp.core.ServiceType
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.AppointmentDto
import com.example.ibrasaloonapp.network.model.AppointmentDtoMapper
import com.example.ibrasaloonapp.network.services.AppointmentService
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.*
import javax.inject.Inject

private const val TAG = "AppointmentRepositoryIm"

class AppointmetRepositoryImpl
@Inject
constructor(
    private val service: AppointmentService,
    private val mapper: AppointmentDtoMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppointmentRepository {


    override suspend fun getAppointments(): ApiResult<List<Appointment>> {
        return safeApiCall(dispatcher = dispatcher) {
            val appointmentsDto = service.getAppointments().appointments.map {
                AppointmentDto(
                    id = it.id,
                    customer = it.customer,
                    type = it.type?.capitalize(Locale.getDefault()),
                    date = it.date?.let { stringDateFormat(it) } ?: "",
                    time = it.time,
                    isActive = it.isActive,
                    createdAt = it.createdAt
                )
            }
            mapper.toDomainList(appointmentsDto)
        }
    }

    override suspend fun bookAppointment(
        appointment: AppointmentDto
    ): ApiResult<String> {
        return safeApiCall(dispatcher = dispatcher) {
            service.bookAppointment(appointment).message
        }
    }

    override suspend fun updateAppointment() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAppointment(id: String): ApiResult<String> {
        return safeApiCall(dispatcher = dispatcher) {
            service.deleteAppointment(id).message
        }
    }

    override suspend fun getServiceType(): ApiResult<List<String>> {
        return ApiResult.Success(fakeServiceType.map { tp ->
            tp.value.capitalize(Locale.getDefault())
        })
    }

    override suspend fun getAvailableAppointments(date: String): ApiResult<List<String>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.getAvailableAppointments(date = date).times
        }
    }

}


val fakeAppointments = listOf<Appointment>(
    Appointment(id = "1", type = "Hair Cut", date = "20/5/2022"),
    Appointment(id = "2", type = "Wax", date = "22/3/2022"),
    Appointment(id = "3", type = "Hair Cut", date = "30/6/2022"),
    Appointment(id = "4", type = "Hair Cut", date = "22/5/2022"),
    Appointment(id = "5", type = "Hair Cut", date = "26/5/2022"),
    Appointment(id = "6", type = "Hair Cut", date = "20/5/2022"),
    Appointment(id = "7", type = "Hair Cut", date = "4/5/2022"),
    Appointment(id = "8", type = "Hair Cut", date = "18/5/2022"),
    Appointment(id = "9", type = "Hair Cut", date = "17/5/2022"),
    Appointment(id = "10", type = "Wax", date = "16/5/2022"),
    Appointment(id = "11", type = "Wax", date = "15/9/2022"),
    Appointment(id = "12", type = "Hair Cut", date = "20/2/2022"),
    Appointment(id = "13", type = "Hair Cut", date = "20/1/2022"),
    Appointment(id = "14", type = "Hair Cut", date = "30/6/2022"),
    Appointment(id = "15", type = "Hair Cut", date = "22/5/2022"),
    Appointment(id = "16", type = "Hair Cut", date = "26/5/2022"),
    Appointment(id = "17", type = "Hair Cut", date = "20/5/2022"),
    Appointment(id = "18", type = "Hair Cut", date = "4/5/2022"),
    Appointment(id = "19", type = "Hair Cut", date = "18/5/2022"),
    Appointment(id = "20", type = "Hair Cut", date = "17/5/2022"),
)


val fakeAvailableDates = listOf(
    "12/3/2022",
    "30/5/2022",
    "6/6/2022",
    "5/7/2022",
    "3/8/2022",
)


val fakeServiceType = ServiceType.values()