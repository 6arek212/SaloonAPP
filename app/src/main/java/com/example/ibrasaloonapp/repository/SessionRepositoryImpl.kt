package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.Session
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.SessionDtoMapper
import com.example.ibrasaloonapp.network.services.SessionListService
import com.example.trainingapp.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class SessionRepositoryImpl
@Inject
constructor(
    private val service: SessionListService,
    private val mapper: SessionDtoMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SessionRepository {


    override suspend fun getSessions(): ApiResult<List<Session>> {
//        return safeApiCall(dispatcher = dispatcher) {
//            val sessionsDto = service.getSessionList().sessions
//            mapper.toDomainList(sessionsDto)
//        }

        return ApiResult.Success(fakeSessions)
    }


    override suspend fun getAvailableAppointments(): ApiResult<List<String>> {
        return ApiResult.Success(fakeAvailableDates)
    }

    override suspend fun getServiceType(): ApiResult<List<String>> {
        return ApiResult.Success(fakeServiceType)
    }
}


val fakeSessions = listOf<Session>(
    Session(id = "1", type = "Hair Cut", date = "20/5/2022"),
    Session(id = "2", type = "Wax", date = "22/3/2022"),
    Session(id = "3", type = "Hair Cut", date = "30/6/2022"),
    Session(id = "4", type = "Hair Cut", date = "22/5/2022"),
    Session(id = "5", type = "Hair Cut", date = "26/5/2022"),
    Session(id = "6", type = "Hair Cut", date = "20/5/2022"),
    Session(id = "7", type = "Hair Cut", date = "4/5/2022"),
    Session(id = "8", type = "Hair Cut", date = "18/5/2022"),
    Session(id = "9", type = "Hair Cut", date = "17/5/2022"),
    Session(id = "10", type = "Wax", date = "16/5/2022"),
    Session(id = "11", type = "Wax", date = "15/9/2022"),
    Session(id = "12", type = "Hair Cut", date = "20/2/2022"),
    Session(id = "13", type = "Hair Cut", date = "20/1/2022"),
    Session(id = "14", type = "Hair Cut", date = "30/6/2022"),
    Session(id = "15", type = "Hair Cut", date = "22/5/2022"),
    Session(id = "16", type = "Hair Cut", date = "26/5/2022"),
    Session(id = "17", type = "Hair Cut", date = "20/5/2022"),
    Session(id = "18", type = "Hair Cut", date = "4/5/2022"),
    Session(id = "19", type = "Hair Cut", date = "18/5/2022"),
    Session(id = "20", type = "Hair Cut", date = "17/5/2022"),
)


val fakeAvailableDates = listOf(
    "12/3/2022",
    "30/5/2022",
    "6/6/2022",
    "5/7/2022",
    "3/8/2022",
)


val fakeServiceType = listOf(
    "Hair Cut",
    "Wax",
    "Massage",
    "Lining"
)