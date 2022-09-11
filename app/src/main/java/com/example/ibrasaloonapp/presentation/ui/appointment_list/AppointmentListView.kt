package com.example.ibrasaloonapp.presentation.ui.appointment_list

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.samples.crane.base.CraneScreen
import androidx.compose.samples.crane.base.CraneTabBar
import androidx.compose.samples.crane.base.CraneTabs
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.home.HomeEvent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppointmentListView(
    navController: NavController,
    viewModel: AppointmentsListViewModel = hiltViewModel(),
    authViewModel: MainActivityViewModel = hiltViewModel()
) {
//    LaunchedEffect(Unit) {
//        viewModel.onTriggerEvent(AppointmentListEvent.GetAppointments)
//    }

    val queue = viewModel.state.value.errorQueue
    val isRefreshing = viewModel.state.value.isRefreshing
    val progressBar = viewModel.state.value.progressBarState
    val appointments = viewModel.state.value.appointments


    DefaultScreenUI(
        onRemoveHeadFromQueue = { viewModel.onTriggerEvent(AppointmentListEvent.OnRemoveHeadFromQueue) },
        queue = queue,
        progressBarState = progressBar,
        dialogOnConfirm = {}
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
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



            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { viewModel.onTriggerEvent(AppointmentListEvent.Refresh) }) {

                LazyColumn(
                    modifier=Modifier.fillMaxSize(),
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
                        AppointmentCard(
                            modifier = Modifier,
                            appointment = appointment, onUnbook = {
                                viewModel.onTriggerEvent(
                                    AppointmentListEvent.UnBookAppointment(
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











