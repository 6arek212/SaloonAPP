package com.example.ibrasaloonapp.presentation.ui.user_details

sealed class UserDetailsEvent {

    class GetUser(val userId: String) : UserDetailsEvent()
    class BlockDialogVisibility(val visible: Boolean) : UserDetailsEvent()

}