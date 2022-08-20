package com.example.ibrasaloonapp.repository

import com.example.ibrasaloonapp.domain.model.Session
import com.example.ibrasaloonapp.network.ApiResult

interface SessionRepository {

    suspend fun getSessions(): ApiResult<List<Session>>

}