package com.example.ibrasaloonapp.domain.model

sealed class ProgressBarState {

    object Loading : ProgressBarState()

    object Idle : ProgressBarState()
}