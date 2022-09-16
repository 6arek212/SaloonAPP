package com.example.ibrasaloonapp.presentation.ui.appointment_list

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.repository.AppointmentRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.trainingapp.network.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "AppointmentsListVM"

@HiltViewModel
class AppointmentsListViewModel
@Inject
constructor(
    private val context: Application,
    private val repository: AppointmentRepository
) : BaseViewModel() {

    private val _state: MutableState<AppointmentListState> = mutableStateOf(AppointmentListState())
    val state: State<AppointmentListState> = _state


    init {
        onTriggerEvent(AppointmentListEvent.GetAppointments)
    }

    fun onTriggerEvent(event: AppointmentListEvent) {
        viewModelScope.launch {
            when (event) {
                is AppointmentListEvent.Refresh -> {
                    _state.value = _state.value.copy(isRefreshing = true)
                    getAppointments()
                    _state.value = _state.value.copy(isRefreshing = false)
                }
                is AppointmentListEvent.GetAppointments -> {
                    getAppointments()
                }
                is AppointmentListEvent.OnRemoveHeadFromQueue -> {
                    removeMessage()
                }
                is AppointmentListEvent.UnBookAppointment -> {
                    unbook(id = event.id, index = event.index)
                }
            }
        }
    }

    private suspend fun getAppointments() {
        loading(true)
        val result = repository.getAppointments()

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(appointments = result.value)
            }

            is ApiResult.GenericError -> {

                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = result.code.defaultErrorMessage(context)
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
            }
        }
        loading(false)
    }


    suspend fun unbook(id: String, index: Int) {
        loading(true)
        val result = repository.unbookAppointment(id)

        when (result) {
            is ApiResult.Success -> {
                val list = ArrayList(_state.value.appointments)
                list.removeAt(index)
                _state.value = _state.value.copy(appointments = list)
                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.unbook),
                        description = context.getString(R.string.your_appointment_has_been_unbooked)

                    )
                )
            }

            is ApiResult.GenericError -> {
                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = result.code.defaultErrorMessage(context)
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                sendMessage(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
            }
        }

        loading(false)
    }

}