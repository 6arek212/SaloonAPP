package com.example.ibrasaloonapp.core.domain

sealed class UIComponent {

    data class Dialog(
        val title: String,
        val description: String,
        val actionButtons: Boolean = false,
        val dialogEvent: DialogEvent? = null
    ) : UIComponent()

    data class Snackbar(
        val message: String,
    ) : UIComponent()

    data class None(
        val message: String,
    ) : UIComponent()
}


sealed class DialogEvent {
    object Logout : DialogEvent()
    class Unbook(val id: String, val index: Int) : DialogEvent()
}