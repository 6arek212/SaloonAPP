package com.example.ibrasaloonapp.presentation.ui.appointment_list

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.DialogEvent
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.use.GetAppointmentsUseCase
import com.example.ibrasaloonapp.use.UnBookAppointmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "AppointmentListViewMode"

@HiltViewModel
class AppointmentsListViewModel
@Inject
constructor(
    private val context: Application,
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val unBookAppointmentUseCase: UnBookAppointmentUseCase
) : BaseViewModel() {

    private val _state: MutableState<AppointmentListState> = mutableStateOf(AppointmentListState())
    val state: State<AppointmentListState> = _state


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

                is AppointmentListEvent.ShowUnbookConfirmDialog -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.unbook),
                            description = context.getString(R.string.are_you_sure_unbook_your_appointment),
                            actionButtons = true,
                            dialogEvent = DialogEvent.Unbook(id = event.id, index = event.index)
                        )
                    )
                }
            }
        }
    }

    private suspend fun getAppointments() {
        getAppointmentsUseCase().onEach { data ->
            when (data) {
                is Resource.Loading -> {
                    loading(data.value)
                }

                is Resource.Success -> {
                    data.data?.let { appointments ->
                        _state.value = _state.value.copy(appointments = appointments)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = data.message
                        )
                    )
                }
            }

        }.launchIn(viewModelScope)
    }


    suspend fun unbook(id: String, index: Int) {
        unBookAppointmentUseCase(appointmentId = id).onEach { data ->
            when (data) {
                is Resource.Loading -> {
                    loading(data.value)
                }

                is Resource.Success -> {
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

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = data.message
                        )
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

}