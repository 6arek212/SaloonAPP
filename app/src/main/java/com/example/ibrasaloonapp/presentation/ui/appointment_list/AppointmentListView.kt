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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen


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
                    .padding(16.dp)
            ) {

                Text(
                    text = "Appointments",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h2
                )

                Divider(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Gray2)
                )

                Spacer(modifier = Modifier.padding(8.dp))
            }

            LazyVerticalGrid(
                contentPadding = PaddingValues(8.dp),
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if(appointments.isEmpty()){
                    item {
                        Empty(text = "No Appointments")
                    }
                }

                itemsIndexed(
                    items = appointments,
                    key = { index, item -> item.id },
                    span = { index, appointment ->
                        if (index == 0 && appointment.isActive) GridItemSpan(maxLineSpan) else GridItemSpan(maxCurrentLineSpan)
                    }) { index, appointment ->
                    AppointmentCard(appointment = appointment, onUnbook = {
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











