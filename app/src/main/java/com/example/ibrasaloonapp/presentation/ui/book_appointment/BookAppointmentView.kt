package com.example.ibrasaloonapp.presentation.ui.book_appointment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.core.stringDateToDateFormat
import com.example.ibrasaloonapp.core.stringDateToTimeFormat
import com.example.ibrasaloonapp.presentation.components.CustomChip
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.HomeAppBar
import kotlinx.coroutines.launch

private const val TAG = "BookAppointmentView"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookAppointmentView(
    navController: NavController,
    viewModel: BookAppointmentViewModel = hiltViewModel(),
) {

    val workers = viewModel.state.value.workers
    val workingDates = viewModel.state.value.workingDates
    val services = viewModel.state.value.services
    val availableAppointments = viewModel.state.value.availableAppointments

    val selectedWorker = viewModel.state.value.selectedWorker
    val selectedWorkingDate = viewModel.state.value.selectedWorkingDate
    val selectedAppointment = viewModel.state.value.selectedAppointment
    val selectedService = viewModel.state.value.selectedService

    val progressBar = viewModel.state.value.progressBarState
    val queue = viewModel.state.value.errorQueue

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val events = viewModel.events


    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is BookAppointmentUIEvent.ExpandSheet -> {
                        sheetState.expand()
                    }

                    is BookAppointmentUIEvent.HideSheet -> {
                        sheetState.collapse()
                    }

                    is BookAppointmentUIEvent.OnBookAppointment -> {

                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            HomeAppBar()
        }
    ) {

        DefaultScreenUI(
            modifier = Modifier.padding(it),
            queue = queue,
            progressBarState = progressBar,
            onRemoveHeadFromQueue = { viewModel.onTriggerEvent(BookAppointmentEvent.OnRemoveHeadFromQueue) }) {

            BottomSheetScaffold(sheetGesturesEnabled = false, scaffoldState = scaffoldState,
                sheetContent = {
                    BookAppointmentConfirmation(
                        pickedDate = selectedAppointment?.startTime,
                        service = selectedService,
                        workerName = "${selectedWorker?.firstName} ${selectedWorker?.lastName}",
                        onBook = { viewModel.onTriggerEvent(BookAppointmentEvent.Book) }
                    )
                }) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(scrollState)
                ) {

                    Text(text = "Book An Appointment", style = MaterialTheme.typography.h3)

                    Spacer(modifier = Modifier.padding(16.dp))

                    Text(text = "Worker:")

                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        for (worker in workers) {
                            CustomChip(
                                text = "${worker.firstName} ${worker.lastName}",
                                onClick = {
                                    viewModel.onTriggerEvent(
                                        BookAppointmentEvent.OnSelectedWorker(worker)
                                    )
                                }, isSelected = worker == selectedWorker
                            )
                        }
                    }


                    AnimatedVisibility(visible = selectedWorker != null) {
                        Column() {
                            Text(text = "Pick a day:")

                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                for (workingDate in workingDates) {
                                    CustomChip(
                                        text = stringDateToDateFormat(workingDate.date),
                                        onClick = {
                                            viewModel.onTriggerEvent(
                                                BookAppointmentEvent.OnSelectedWorkingDate(
                                                    workingDate
                                                )
                                            )
                                        }, isSelected = workingDate == selectedWorkingDate
                                    )
                                }
                            }
                        }
                    }



                    AnimatedVisibility(visible = selectedWorkingDate != null) {

                        Column() {
                            Text(text = "Service:")

                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {

                                for (ser in services) {
                                    CustomChip(
                                        text = ser,
                                        onClick = {
                                            viewModel.onTriggerEvent(
                                                BookAppointmentEvent.OnSelectedService(ser)
                                            )
                                        }, isSelected = ser == selectedService
                                    )
                                }
                            }
                        }
                    }



                    AnimatedVisibility(visible = selectedService.isNotBlank()) {
                        Column() {
                            Text(text = "Pick an appointment:")

                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                for (appointment in availableAppointments) {
                                    CustomChip(
                                        text = appointment.startTime?.let {
                                            stringDateToTimeFormat(
                                                appointment.startTime
                                            )
                                        } ?: "",
                                        onClick = {
                                            viewModel.onTriggerEvent(
                                                BookAppointmentEvent.OnSelectedAppointment(
                                                    appointment
                                                )
                                            )
                                        }, isSelected = appointment == selectedAppointment
                                    )
                                }
                            }
                        }

                    }


                }


            }

        }
    }
}


@Composable
fun BookAppointmentConfirmation(
    pickedDate: String? = null,
    service: String? = null,
    workerName: String? = null,
    onBook: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f)
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Appointment Information")

        Spacer(modifier = Modifier.padding(16.dp))

        Text(text = "${pickedDate?.let { stringDateFormat(pickedDate) }} for a ${service}")

        Text(text = "With ${workerName}")


        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = { onBook() }) {
            Text(text = "Confirm And Book")
        }
    }


}