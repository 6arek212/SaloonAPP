package com.example.ibrasaloonapp.presentation.ui.customer_list

import com.example.ibrasaloonapp.domain.model.User

data class CustomersListState(
    val search: String = "",
    val users: List<User> = listOf(),
    val usersCount: Int? = null,
    val newUsersCount: Int? = null

)