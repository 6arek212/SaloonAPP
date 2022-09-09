package com.example.ibrasaloonapp.presentation.ui.book_appointment

import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.core.stringDateToDateFormat
import com.example.ibrasaloonapp.core.stringDateToTimeFormat
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
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

    val chipModifier = Modifier.padding(end = 4.dp)

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
                        sheetState.collapse()
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

            BottomSheetScaffold(
                backgroundColor = Gray2,
                sheetBackgroundColor = White,
                sheetGesturesEnabled = false,
                scaffoldState = scaffoldState,
                sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
                sheetElevation = 8.dp,
                sheetContent = {
                    BookAppointmentConfirmation(
                        pickedDate = selectedAppointment?.startTime,
                        service = selectedService,
                        workerName = "${selectedWorker?.firstName} ${selectedWorker?.lastName}",
                        onBook = { viewModel.onTriggerEvent(BookAppointmentEvent.Book) },
                        sheetState = sheetState
                    )
                }) {

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .verticalScroll(scrollState)
                ) {

                    SubTitle(text = stringResource(id = R.string.book_appointment))

                    Spacer(modifier = Modifier.padding(16.dp))

                    Text(
                        text = stringResource(id = R.string.pick_worker),
                        style = MaterialTheme.typography.h4
                    )

                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        for (worker in workers) {
                            ImageChip(
                                modifier = chipModifier,
                                text = "${worker.firstName} ${worker.lastName}",
                                onClick = {
                                    viewModel.onTriggerEvent(
                                        BookAppointmentEvent.OnSelectedWorker(worker)
                                    )
                                }, isSelected = worker == selectedWorker,
                                url = worker.image
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(16.dp))

                    AnimatedVisibility(visible = selectedWorker != null) {
                        Column() {
                            Text(
                                text = stringResource(id = R.string.pick_day),
                                style = MaterialTheme.typography.h4
                            )

                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                for (workingDate in workingDates) {
                                    CustomChip(
                                        modifier = chipModifier,
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


                    Spacer(modifier = Modifier.padding(16.dp))

                    AnimatedVisibility(visible = selectedWorkingDate != null) {

                        Column() {
                            Text(
                                text = stringResource(id = R.string.pick_service),
                                style = MaterialTheme.typography.h4
                            )

                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {

                                for (ser in services) {
                                    CustomChip(
                                        modifier = chipModifier,
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

                    Spacer(modifier = Modifier.padding(16.dp))


                    AnimatedVisibility(visible = selectedService.isNotBlank()) {
                        Column() {
                            Text(
                                text = stringResource(id = R.string.pick_appointment),
                                style = MaterialTheme.typography.h4
                            )

                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                for (appointment in availableAppointments) {
                                    CustomChip(
                                        modifier = chipModifier,
                                        text = appointment.startTime?.let {
                                            stringDateToTimeFormat(
                                                appointment.startTime
                                            )
                                        },
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookAppointmentConfirmation(
    pickedDate: String? = null,
    service: String? = null,
    workerName: String? = null,
    onBook: () -> Unit,
    sheetState: BottomSheetState
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f)
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.appointment_info),
                style = MaterialTheme.typography.h4
            )
            IconButton(modifier = Modifier.align(Alignment.CenterVertically), onClick = {
                scope.launch { sheetState.collapse() }
            }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "CLOSE")
            }
        }


        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "${pickedDate?.let { stringDateFormat(pickedDate) }} ${stringResource(id = R.string.for_a)} ${service}",
            style = MaterialTheme.typography.body1
        )

        Text(text = "${stringResource(id = R.string.with)} ${workerName}", style = MaterialTheme.typography.body1)


        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            modifier = Modifier,
            onClick = { onBook() },
            shape = MaterialTheme.shapes.small,
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 34.dp)
        ) {
            Text(
                text = stringResource(id = R.string.confirm_and_book),
                style = MaterialTheme.typography.h5
            )
        }
    }


}