package com.example.ibrasaloonapp.presentation.ui.book_appointment

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.TimePatterns
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.Service
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import kotlinx.coroutines.launch

private const val TAG = "BookAppointmentView"
const val APPOINTMENT_KEY = "APPOINTMENT_KEY"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookAppointmentView(
    navController: NavController,
    mainViewModel: MainActivityViewModel,
    viewModel: BookAppointmentViewModel = hiltViewModel(),
    popBackStack: (appointment: Appointment) -> Unit
) {

    val workers = viewModel.state.value.workers
    val workingDates = viewModel.state.value.workingDates
    val services = viewModel.state.value.services
    val availableAppointments = viewModel.state.value.availableAppointments
    val userId = mainViewModel.state.value.authData?.user?.id

    val selectedWorker = viewModel.state.value.selectedWorker
    val selectedWorkingDate = viewModel.state.value.selectedWorkingDate
    val selectedAppointment = viewModel.state.value.selectedAppointment
    val selectedService = viewModel.state.value.selectedService

    val progressBar = viewModel.uiState.collectAsState().value.progressBarState
    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val events = viewModel.events
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    BackHandler(sheetState.isVisible) {
        scope.launch { sheetState.hide() }
    }

    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is BookAppointmentUIEvent.ExpandSheet -> {
                        sheetState.show()
                    }

                    is BookAppointmentUIEvent.HideSheet -> {
                        sheetState.hide()
                    }

                    is BookAppointmentUIEvent.OnBookAppointment -> {
                        sheetState.hide()
                        popBackStack(event.appointment)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            HomeAppBar()
        },
    ) {

        DefaultScreenUI(
            modifier = Modifier.padding(it),
            uiComponent = uiMessage,
            progressBarState = progressBar,
            onRemoveUIComponent = { viewModel.onTriggerEvent(BookAppointmentEvent.OnRemoveHeadFromQueue) }) {

            ModalBottomSheetLayout(
                modifier = Modifier.fillMaxSize(),
                sheetBackgroundColor = White,
                sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
                sheetElevation = 16.dp,
                sheetState = sheetState,
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
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .background(Gray2)
                        .padding(
                            top = 8.dp,
                            bottom = BottomSheetScaffoldDefaults.SheetPeekHeight + 16.dp
                        )
                ) {

                    TitleSection(popBackStack = navController::popBackStack)


                    Spacer(modifier = Modifier.padding(16.dp))


                    Workers(
                        workers = workers,
                        onTriggerEvent = viewModel::onTriggerEvent,
                        selectedWorker = selectedWorker,
                    )


                    Spacer(modifier = Modifier.padding(16.dp))


                    PickDay(
                        selectedWorker = selectedWorker,
                        workingDates = workingDates,
                        onTriggerEvent = viewModel::onTriggerEvent,
                        selectedWorkingDate = selectedWorkingDate
                    )


                    Spacer(modifier = Modifier.padding(16.dp))

                    PickService(
                        selectedWorkingDate = selectedWorkingDate,
                        services = services,
                        onTriggerEvent = viewModel::onTriggerEvent,
                        selectedService = selectedService
                    )

                    Spacer(modifier = Modifier.padding(16.dp))


                    PickAppointment(
                        selectedAppointment = selectedAppointment,
                        availableAppointments = availableAppointments,
                        onTriggerEvent = viewModel::onTriggerEvent,
                        selectedService = selectedService
                    )
                }
            }
        }

    }
}

@Composable
fun TitleSection(popBackStack: () -> Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        BackButton(onClick = { popBackStack() }, tint = MaterialTheme.colors.primary)

        Spacer(modifier = Modifier.padding(4.dp))

        SubTitle(
            modifier = Modifier.padding(),
            text = stringResource(id = R.string.book_appointment)
        )
    }
}

@Composable
fun PickAppointment(
    selectedAppointment: Appointment?,
    availableAppointments: List<Appointment>,
    onTriggerEvent: (BookAppointmentEvent) -> Unit,
    selectedService: Service?
) {
    AnimatedVisibility(visible = selectedService != null) {
        Column() {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.pick_appointment),
                style = MaterialTheme.typography.h4
            )


            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
            ) {
                items(items = availableAppointments) { appointment ->
                    CustomChip(
                        text = stringDateFormat(
                            appointment.startTime,
                            TimePatterns.TIME_ONLY,
                            LocalContext.current
                        ),
                        onClick = {
                            onTriggerEvent(
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

@Composable
fun PickService(
    selectedWorkingDate: String?,
    services: List<Service>,
    onTriggerEvent: (BookAppointmentEvent) -> Unit,
    selectedService: Service?
) {
    AnimatedVisibility(visible = selectedWorkingDate != null) {

        Column() {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.pick_service),
                style = MaterialTheme.typography.h4
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)
            ) {
                items(items = services) { ser ->
                    HeaderChip(
                        text = ser.title,
                        headerText = "${ser.price}₪",
                        onClick = {
                            onTriggerEvent(
                                BookAppointmentEvent.OnSelectedService(ser)
                            )
                        },
                        isSelected = ser.id == selectedService?.id
                    )
                }
            }
        }
    }
}

@Composable
fun PickDay(
    selectedWorker: User?,
    workingDates: List<String>,
    onTriggerEvent: (BookAppointmentEvent) -> Unit,
    selectedWorkingDate: String?
) {

    AnimatedVisibility(visible = selectedWorker != null) {
        Column() {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.pick_day),
                style = MaterialTheme.typography.h4
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
            ) {
                items(items = workingDates) { workingDate ->
                    CustomChip(
                        text = stringDateFormat(
                            workingDate,
                            TimePatterns.DATE_MM_DD,
                            LocalContext.current
                        ),
                        onClick = {
                            onTriggerEvent(
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
}


@Composable
private fun Workers(
    modifier: Modifier = Modifier,
    workers: List<User>,
    onTriggerEvent: (BookAppointmentEvent) -> Unit,
    selectedWorker: User?
) {
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = stringResource(id = R.string.pick_worker),
        style = MaterialTheme.typography.h4
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
    ) {
        items(items = workers) { worker ->
            ImageChip(
                text = "${worker.firstName} ${worker.lastName}",
                onClick = {
                    onTriggerEvent(
                        BookAppointmentEvent.OnSelectedWorker(worker)
                    )
                },
                isSelected = worker == selectedWorker,
                url = worker.image,
                imageSize = 55.dp
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookAppointmentConfirmation(
    pickedDate: String? = null,
    service: Service? = null,
    workerName: String? = null,
    onBook: () -> Unit,
    sheetState: ModalBottomSheetState
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.appointment_info),
                style = MaterialTheme.typography.h4
            )
            IconButton(modifier = Modifier.align(Alignment.CenterVertically), onClick = {
                scope.launch { sheetState.hide() }
            }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "CLOSE")
            }
        }


        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "${
                pickedDate?.let {
                    stringDateFormat(
                        pickedDate,
                        TimePatterns.DAY_ONLY,
                        LocalContext.current
                    )
                }
            }," +
                    " ${
                        pickedDate?.let {
                            stringDateFormat(
                                pickedDate,
                                TimePatterns.REGULAR_DATE,
                                LocalContext.current
                            )
                        }
                    }" +
                    " ${stringResource(id = R.string.at)}" +
                    " ${
                        pickedDate?.let {
                            stringDateFormat(
                                pickedDate,
                                TimePatterns.TIME_ONLY,
                                LocalContext.current
                            )
                        }
                    }" +
                    " ${stringResource(id = R.string.for_a)} ${service?.title}",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )

        Text(
            text = "${stringResource(id = R.string.with)} ${workerName}",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )


        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            modifier = Modifier,
            onClick = onBook,
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