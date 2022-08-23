package com.example.ibrasaloonapp.core.domain

sealed class ProgressBarState {

    object Loading : ProgressBarState()

    object Idle : ProgressBarState()
}