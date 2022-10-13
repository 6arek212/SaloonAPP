package com.example.ibrasaloonapp.presentation.ui.users_list


sealed class UsersListEvent {

    object GetUsersList : UsersListEvent()
    object Refresh : UsersListEvent()
    class OnSearchChanged(val search: String) : UsersListEvent()
    class UpdateCustomer(val id: String) : UsersListEvent()

}