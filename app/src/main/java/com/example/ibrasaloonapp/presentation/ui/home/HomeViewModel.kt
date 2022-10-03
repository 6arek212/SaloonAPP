package com.example.ibrasaloonapp.presentation.ui.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.use.GetAppointmentUseCase
import com.example.ibrasaloonapp.use.GetWorkerServicesUseCase
import com.example.ibrasaloonapp.use.GetWorkersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val context: Application,
    private val getAppointmentUseCase: GetAppointmentUseCase,
    private val getWorkersUseCase: GetWorkersUseCase,
) : BaseViewModel() {

    private val _state: MutableState<HomeState> = mutableStateOf(HomeState())
    val state: State<HomeState> = _state


    fun onTriggerEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.GetData -> {
                    if (!event.isAuthed) {
                        _state.value = _state.value.copy(
                            appointment = null,
                            refreshing = false
                        )
                        getWorkers()
                    } else {
                        getAppointment()
                    }
                }


                is HomeEvent.OnRemoveHeadFromQueue -> {
                    removeMessage()
                }

                is HomeEvent.Refresh -> {
                    if (!isLoading()) {
                        _state.value = _state.value.copy(refreshing = true)
                        if (!event.isAuthed) {
                            _state.value = _state.value.copy(
                                appointment = null,
                                refreshing = false
                            )
                            getWorkers()
                        } else {
                            getAppointment()
                        }
                    }
                }
            }

        }
    }



    private suspend fun getWorkers() {

        getWorkersUseCase().onEach {
            when (it) {

                is Resource.Loading -> {
                    loading(it.value)
                    if (!it.value && _state.value.refreshing) {
                        _state.value = _state.value.copy(refreshing = false)
                    }
                }

                is Resource.Success -> {
                    it.data?.let { workers ->
                        _state.value = _state.value.copy(workers = workers)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = it.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    private suspend fun getAppointment() {
        getAppointmentUseCase().onEach {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                    if (!it.value && _state.value.refreshing) {
                        _state.value = _state.value.copy(refreshing = false)
                    }
                }

                is Resource.Success -> {
                    Log.d(TAG, "getAppointment:------------- ${it.data}")
                    _state.value = _state.value.copy(appointment = it.data)
                    getWorkers()
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = it.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}