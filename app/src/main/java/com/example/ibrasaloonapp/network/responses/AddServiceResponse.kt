package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.ServiceDto

data class AddServiceResponse(
    val message: String,
    val service: ServiceDto
) {
}