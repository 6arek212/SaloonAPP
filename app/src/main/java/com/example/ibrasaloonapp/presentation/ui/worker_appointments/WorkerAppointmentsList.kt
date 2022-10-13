package com.example.ibrasaloonapp.presentation.ui.worker_appointments

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material.icons.outlined.DesignServices
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.Service
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.services.ServicesSheetContent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkerAppointmentsList(
    mainViewModel: MainActivityViewModel,
    viewModel: WorkerAppointmentsListViewModel = hiltViewModel(),
    navigateToCreateAppointment: () -> Unit,
) {
    val isRefreshing = viewModel.state.value.isRefreshing
    val appointments = viewModel.state.value.appointments
    val dates = viewModel.state.value.dates
    val selectedDate = viewModel.state.value.selectedDate
    val dateRange = viewModel.state.value.dateRange
    val search = viewModel.state.value.search
    val dialogVisibility = viewModel.state.value.updateStatusDialogVisibility
    val progress = viewModel.uiState.collectAsState().value.progressBarState
    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage
    val filter = viewModel.state.value.filter
    val user = mainViewModel.state.value.authData?.user
    val services = viewModel.state.value.services

    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(WorkerAppointmentsListEvent.GetAppointments)
    }


    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


    BackHandler(sheetState.isVisible) {
        scope.launch { sheetState.hide() }
    }

    DefaultScreenUI(
        uiComponent = uiMessage,
        progressBarState = progress,
        onRemoveUIComponent = { viewModel.onTriggerEvent(WorkerAppointmentsListEvent.DismissUIMessage) }) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.onTriggerEvent(WorkerAppointmentsListEvent.Refresh) }
        ) {
            OptionDialog(
                isVisible = dialogVisibility,
                onDismiss = { viewModel.onTriggerEvent(WorkerAppointmentsListEvent.DismissStatusDialog) },
                onUpdate = { option ->

                    when (option) {

                        is UpdateAppointmentEvent.DeleteAppointment -> {
                            viewModel.onTriggerEvent(
                                WorkerAppointmentsListEvent.DeleteAppointment
                            )
                        }

                        is UpdateAppointmentEvent.UpdateStatus -> {
                            viewModel.onTriggerEvent(
                                WorkerAppointmentsListEvent.UpdateAppointmentStatus(
                                    status = option.status
                                )
                            )
                        }
                    }
                }
            )
            ModalBottomSheetLayout(
                sheetState = sheetState,
                sheetContent = {
                    ServicesSheetContent(
                        services = services,
                        onAddingService = { title, price ->
                            viewModel.onTriggerEvent(
                                WorkerAppointmentsListEvent.AddService(title, price)
                            )
                        },
                        onDeleteService = { serviceId, index ->
                            serviceId?.let {
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.DeleteService(
                                        serviceId = serviceId,
                                        index = index
                                    )
                                )
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxSize()
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
                        BackLayer(
                            user = user,
                            filter = filter,
                            dates = dates,
                            selectedDate = selectedDate,
                            dateRange = dateRange,
                            search = search,
                            onSearchChange = { str ->
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.OnSearchChange(str)
                                )
                            },
                            onSelectedDate = { item ->
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.OnSelectedDate(
                                        item
                                    )
                                )
                            },
                            decreaseWeekRange = {
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.DecreaseWeekRange
                                )
                            },
                            increaseWeekRange = {
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.IncreaseWeekRange
                                )
                            },
                            onSearch = { viewModel.onTriggerEvent(WorkerAppointmentsListEvent.Search) },
                            changeFilter = { filter ->
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.ChangeFilter(filter)
                                )
                            },
                            showServicesBottomSheet = {
                                viewModel.onTriggerEvent(WorkerAppointmentsListEvent.GetServices)
                                scope.launch { sheetState.show() }
                            }
                        )
                    },
                    frontLayerContent = {
                        FrontLayer(
                            appointments = appointments,
                            showStatusDialog = { id, index ->
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.ShowStatusDialog(
                                        id = id,
                                        index = index
                                    )
                                )
                            },
                            navigateToCreateAppointment = navigateToCreateAppointment
                        )
                    })
            }
        }
    }


}


@Composable
private fun OptionDialog(
    isVisible: Boolean,
    onUpdate: (UpdateAppointmentEvent) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible)
        return
    Dialog(onDismissRequest = onDismiss) {
        UpdateAppointmentDialog(onUpdate = onUpdate)
    }
}


@Composable
private fun BackLayer(
    user: User?,
    filter: AppointmentFilter,
    search: String,
    dates: List<DayCardData>,
    selectedDate: DayCardData?,
    dateRange: DateRange?,
    decreaseWeekRange: () -> Unit,
    increaseWeekRange: () -> Unit,
    onSelectedDate: (DayCardData) -> Unit,
    onSearchChange: (String) -> Unit,
    onSearch: () -> Unit,
    changeFilter: (AppointmentFilter) -> Unit,
    showServicesBottomSheet: () -> Unit
) {

    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        if (user != null)
            Row(modifier = Modifier.fillMaxWidth()) {
                ImageChip(
                    modifier = Modifier,
                    text = "${user.firstName} ${user.lastName}",
                    onClick = { /*TODO*/ },
                    isSelected = false,
                    url = user.image
                )

                Spacer(modifier = Modifier.padding(8.dp))

                IconButton(onClick = showServicesBottomSheet) {
                    Icon(
                        imageVector = Icons.Outlined.DesignServices,
                        contentDescription = "services"
                    )
                }

            }

        Spacer(modifier = Modifier.padding(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(GrayBlue)
                    .size(36.dp),
                onClick = decreaseWeekRange
            ) {
                Icon(
                    imageVector = if (LocalLayoutDirection.current == LayoutDirection.Ltr) Icons.Outlined.ArrowLeft else Icons.Outlined.ArrowRight,
                    contentDescription = ""
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.calendar),
                contentDescription = "calender"
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(text = "${dateRange?.from}-${dateRange?.to} ${dateRange?.monthString}")

            Spacer(modifier = Modifier.padding(8.dp))

            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(GrayBlue)
                    .size(36.dp),
                onClick = increaseWeekRange
            ) {
                Icon(
                    imageVector = if (LocalLayoutDirection.current == LayoutDirection.Ltr) Icons.Outlined.ArrowRight else Icons.Outlined.ArrowLeft,
                    contentDescription = ""
                )
            }
        }


        Spacer(modifier = Modifier.padding(24.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items(items = dates) { item ->
                DayCard(
                    dayNumber = item.numDay,
                    dayText = item.textDate,
                    isSelected = selectedDate == item,
                    onClick = { onSelectedDate(item) }
                )
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Row(modifier = Modifier.height(IntrinsicSize.Max)) {

            TextField(
                modifier = Modifier.scale(scaleY = 0.9F, scaleX = 1F),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
                        color = Black1,
                        style = MaterialTheme.typography.body2
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                onValueChange = { s ->
                    onSearchChange(s)
                },
                value = search,
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Search, stringResource(R.string.search))
                },
                textStyle = MaterialTheme.typography.body2,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colors.background,
                    textColor = Black2
                ),
                shape = CircleShape,
                maxLines = 1,
            )

            Spacer(modifier = Modifier.padding(2.dp))

            Button(
                modifier = Modifier.padding(4.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(backgroundColor = Orange),
                onClick = onSearch
            ) {
                Text(text = stringResource(R.string.search), style = MaterialTheme.typography.body2)
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Row {

            CustomChip(
                text = stringResource(R.string.show_all),
                onClick = { changeFilter(AppointmentFilter.ShowAll) },
                isSelected = filter == AppointmentFilter.ShowAll
            )

            Spacer(modifier = Modifier.padding(4.dp))

            CustomChip(
                text = stringResource(id = R.string.in_progress),
                onClick = { changeFilter(AppointmentFilter.InProgress) },
                isSelected = filter == AppointmentFilter.InProgress
            )

        }

        Spacer(modifier = Modifier.padding(16.dp))

    }

}


@Composable
private fun FrontLayer(
    appointments: List<Appointment>,
    showStatusDialog: (String, Int) -> Unit,
    navigateToCreateAppointment: () -> Unit,
) {

    Box() {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
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
                        underLine = false
                    )
                }
            }

            itemsIndexed(items = appointments, key = { index, item -> item.id }) { index, item ->
                WorkerAppointmentCard(
                    appointment = item,
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
                    },
                    showOptionDialog = { showStatusDialog(item.id, index) }
                )
            }
        }

        IconButton(onClick = navigateToCreateAppointment) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
        }
    }
}


@Composable
@Preview
fun WorkerAppointmentsListPreview() {
    AppTheme {
//        WorkerAppointmentsList(navigateToCreateAppointment = {},)
    }
}


