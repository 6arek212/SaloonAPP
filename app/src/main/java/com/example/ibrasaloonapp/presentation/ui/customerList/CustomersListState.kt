package com.example.ibrasaloonapp.presentation.ui.customerList

import com.example.ibrasaloonapp.domain.model.User

data class CustomersListState(
    val customers: List<User> = listOf(),
    val customersCount: Int? = null
)