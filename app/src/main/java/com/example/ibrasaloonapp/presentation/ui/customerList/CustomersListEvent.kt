package com.example.ibrasaloonapp.presentation.ui.customerList

sealed class CustomersListEvent {

    object GetCustomersList : CustomersListEvent()
    class UpdateCustomer(val id: String) : CustomersListEvent()

}