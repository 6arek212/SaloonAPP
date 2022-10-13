package com.example.ibrasaloonapp.presentation.ui.user_details


sealed class UserDetailsEvent {

    object Refresh : UserDetailsEvent()
    class GetUser(val userId: String) : UserDetailsEvent()
    class MarkAsBarber(val value: Boolean) : UserDetailsEvent()
    class Block(val value: Boolean) : UserDetailsEvent()

}