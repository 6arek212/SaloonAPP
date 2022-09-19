package com.example.ibrasaloonapp.presentation.ui.appointment_list

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.DialogEvent
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@Composable
fun AppointmentListView(
    navController: NavController,
    viewModel: AppointmentsListViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel
) {

    val uiMessage = viewModel.uiState.value.uiMessage
    val isRefreshing = viewModel.state.value.isRefreshing
    val progressBar = viewModel.uiState.value.progressBarState
    val appointments = viewModel.state.value.appointments


    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(AppointmentListEvent.GetAppointments)
    }

    DefaultScreenUI(
        onRemoveUIComponent = { viewModel.onTriggerEvent(AppointmentListEvent.OnRemoveHeadFromQueue) },
        uiComponent = uiMessage,
        progressBarState = progressBar,
        dialogOnConfirm = { event ->
            when (event) {
                is DialogEvent.Unbook -> {
                    viewModel.onTriggerEvent(
                        AppointmentListEvent.UnBookAppointment(
                            event.id,
                            event.index
                        )
                    )
                }
                else -> {}
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray2)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(id = R.string.appointments),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h2
                )



                Spacer(modifier = Modifier.padding(8.dp))
            }


            val lazyState = rememberLazyListState()
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { viewModel.onTriggerEvent(AppointmentListEvent.Refresh) }) {

                LazyColumn(
                    state = lazyState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (appointments.isEmpty()) {
                        item {
                            Empty(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = stringResource(id = R.string.no_appointments)
                            )
                        }
                    }

                    itemsIndexed(
                        items = appointments,
                        key = { index, item -> item.id }) { index, appointment ->

                        AnimationBox{
                            AppointmentCard(
                                modifier = Modifier,
                                appointment = appointment, onUnbook = {
                                    viewModel.onTriggerEvent(
                                        AppointmentListEvent.ShowUnbookConfirmDialog(
                                            appointment.id,
                                            index
                                        )
                                    )
                                })
                        }
                    }
                }
            }
        }
    }
}










