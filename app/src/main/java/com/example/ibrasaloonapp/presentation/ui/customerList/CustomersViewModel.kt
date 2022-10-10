package com.example.ibrasaloonapp.presentation.ui.customerList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.use.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CustomersViewModel
@Inject
constructor(
    val getUsersUseCase: GetUsersUseCase
) : BaseViewModel() {

    private val _state:MutableState<CustomersListState> = mutableStateOf(CustomersListState())
    val state:State<CustomersListState> = _state



    fun onTriggerEvent(event : CustomersListEvent) {
        viewModelScope.launch {

            when(event){
                is CustomersListEvent.GetCustomersList->{
                    getCustomers()
                }
            }

        }
    }


    private suspend fun getCustomers() {
        getUsersUseCase().collect{ result->

            when(result){

               is Resource.Error->{
                    sendMessage(UIComponent.Snackbar(message = result.message))
                }

               is Resource.Success->{
                   result.data?.let {
                       _state.value = _state.value.copy(customers = it)
                   }
                }

                is Resource.Loading->{
                    loading(result.value)
                }
            }
        }

    }

}