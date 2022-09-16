package com.example.ibrasaloonapp.network.responses


data class WorkingDateResponse(
    val message: String,
    val workingDates: List<String>
) {
}