package com.example.ibrasaloonapp.presentation.ui.customer_list


sealed class CustomersListEvent {

    object GetCustomersList : CustomersListEvent()
    class OnSearchChanged(val search: String) : CustomersListEvent()
    class UpdateCustomer(val id: String) : CustomersListEvent()

}