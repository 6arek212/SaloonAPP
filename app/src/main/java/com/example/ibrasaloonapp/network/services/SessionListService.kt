package com.example.ibrasaloonapp.network.services

import com.example.ibrasaloonapp.network.responses.SessionListResponse
import retrofit2.http.GET

interface SessionListService {

    @GET("")
    suspend fun getSessionList(): SessionListResponse


}