package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.ServiceDto

data class ServicesResponse(
    val message: String,
    val services: List<ServiceDto>
) {
}