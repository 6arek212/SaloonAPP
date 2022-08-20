package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.SessionDto

data class SessionListResponse(
    val count: Int,
    val sessions: List<SessionDto>
) {
}