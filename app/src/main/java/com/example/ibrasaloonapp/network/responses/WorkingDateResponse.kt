package com.example.ibrasaloonapp.network.responses

import com.example.ibrasaloonapp.network.model.WorkingDateDto

data class WorkingDateResponse(
    val message: String,
    val workingDates: List<WorkingDateDto>
) {
}