package com.example.ibrasaloonapp.presentation.ui.users_list

import com.example.ibrasaloonapp.domain.model.User

data class UsersListState(
    val search: String = "",
    val users: List<User> = listOf(),
    val usersCount: Int? = null,
    val newUsersCount: Int? = null,
    val refresh:Boolean = false
)