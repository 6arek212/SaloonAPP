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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.DialogEvent
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppointmentListView(
    navController: NavController,
    viewModel: AppointmentsListViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel
) {

    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage
    val isRefreshing = viewModel.state.value.isRefreshing
    val progressBar = viewModel.uiState.collectAsState().value.progressBarState
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
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.onTriggerEvent(AppointmentListEvent.Refresh) }
        ) {
            BackdropScaffold(
                scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
                backLayerBackgroundColor = MaterialTheme.colors.primary,
                frontLayerBackgroundColor = MaterialTheme.colors.background,
                frontLayerShape = MaterialTheme.shapes.large,
                frontLayerScrimColor = Color.Unspecified,
                appBar = {
                    HomeAppBar()
                },
                backLayerContent = {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.video_time),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        SubTitle(
                            text = stringResource(id = R.string.appointments),
                            textColor = MaterialTheme.colors.onPrimary,
                            underLine = false
                        )
                    }
                },
                frontLayerContent = {
                    FrontLayer(
                        isRefreshing = isRefreshing,
                        appointments = appointments,
                        showUnbookConfirmDialog = { id, index ->
                            viewModel.onTriggerEvent(
                                AppointmentListEvent.ShowUnbookConfirmDialog(
                                    id,
                                    index
                                )
                            )
                        }
                    )
                })
        }
    }
}


@Composable
private fun FrontLayer(
    isRefreshing: Boolean,
    appointments: List<Appointment>,
    showUnbookConfirmDialog: (String, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray2)
    ) {

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(MaterialTheme.colors.primary)
//                .padding(16.dp)
//        ) {
//
//            Text(
//                text = stringResource(id = R.string.appointments),
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h2
//            )
//
//
//
//            Spacer(modifier = Modifier.padding(8.dp))
//        }


        val lazyState = rememberLazyListState()


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

                AnimationBox {
                    AppointmentCard2(
                        modifier = Modifier,
                        appointment = appointment,
                        backgroundColor = when (index % 3) {
                            0 -> PurpleLite
                            1 -> BlueLight
                            2 -> RedLight
                            else -> PurpleLite
                        },
                        statusBackgroundColor = when (index % 3) {
                            0 -> PurpleDark
                            1 -> BlueDark
                            2 -> RedDark
                            else -> PurpleDark
                        }, onUnbook = {
                            showUnbookConfirmDialog(appointment.id, index)
                        }
                    )
                }
            }
        }
    }
}









