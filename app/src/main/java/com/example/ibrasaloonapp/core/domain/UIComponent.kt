package com.example.ibrasaloonapp.core.domain

sealed class UIComponent {

    data class Dialog(
        val title: String,
        val description: String,
        val confirmButton: Boolean = false
    ) : UIComponent()

    data class None(
        val message: String,
    ) : UIComponent()
}