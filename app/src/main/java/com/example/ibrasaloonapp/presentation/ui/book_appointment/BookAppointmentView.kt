package com.example.ibrasaloonapp.presentation.ui.book_appointment

import DropDownMenuComponent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.ibrasaloonapp.presentation.components.DatePicker
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.appointment_list.AppointmentListEvent
import com.example.ibrasaloonapp.presentation.ui.appointment_list.AppointmentsListViewModel
import com.example.ibrasaloonapp.presentation.ui.login.LoginViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "BookAppointmentView"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookAppointmentView(
    navController: NavController,
    viewModel: BookAppointmentViewModel = hiltViewModel(),
) {

    val timeError = viewModel.state.value.timeError
    val dateError = viewModel.state.value.dateError
    val serviceTypeError = viewModel.state.value.serviceTypeError

    val expandDropDown2 = viewModel.state.value.expandDropDown2
    val expandDropDown1 = viewModel.state.value.expandDropDown1
    val typesList = viewModel.state.value.typesList
    val availableAppointmentsTimesList = viewModel.state.value.availableAppointmentsTimesList
    val date = viewModel.state.value.date
    val time = viewModel.state.value.time
    val serviceType = viewModel.state.value.serviceType

    val progressBar = viewModel.state.value.progressBarState
    val queue = viewModel.state.value.errorQueue

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val events = viewModel.events

    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is BookAppointmentViewModel.UIEvent.OnBookAppointment -> {

                        Log.d(TAG, "BookAppointmentView: ")
                    }
                }
            }
        }
    }


    DefaultScreenUI(
        queue = queue,
        progressBarState = progressBar,
        onRemoveHeadFromQueue = { viewModel.onTriggerEvent(BookAppointmentEvent.OnRemoveHeadFromQueue) }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        )
        {

            Image(
                painter = painterResource(id = R.drawable.barber_shop_brief),
                contentDescription = "",
                modifier = Modifier
                    .heightIn(min = 200.dp)
                    .widthIn(min = 200.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp)

            )




            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(scrollState)
            ) {

                Text(text = "Book An Appointment", style = MaterialTheme.typography.overline)

                Divider(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(.5.dp), color = MaterialTheme.colors.onBackground
                )

                Spacer(modifier = Modifier.padding(16.dp))



                DatePicker(
                    error = dateError,
                    label = "Pick Date",
                    datePicked = date,
                    updatedDate = { updatedDate ->
                        viewModel.onTriggerEvent(BookAppointmentEvent.DateChanged(updatedDate))
                    })

                Spacer(modifier = Modifier.padding(16.dp))


                DropDownMenuComponent(
                    error = timeError,
                    label = "Pick time",
                    expanded = expandDropDown2,
                    selectedOptionText = time,
                    onExpandedChange = {
                        viewModel.onTriggerEvent(BookAppointmentEvent.TimeDropDownExpandChange(!expandDropDown2))
                    },
                    onDismissRequest = {
                        viewModel.onTriggerEvent(BookAppointmentEvent.TimeDropDownExpandChange(false))
                    }
                ) {
                    for (time in availableAppointmentsTimesList) {

                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onClick = {
                                viewModel.onTriggerEvent(
                                    BookAppointmentEvent.TimeDropDownExpandChange(
                                        false
                                    )
                                )
                                viewModel.onTriggerEvent(BookAppointmentEvent.TimeChanged(time))
                            }) {
                            Column {

                                Text(
                                    text = time,
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onSurface
                                )

                                Divider(
                                    modifier = Modifier
                                        .padding(top = 8.dp, bottom = 8.dp)
                                        .fillMaxWidth()
                                        .height(.5.dp), color = MaterialTheme.colors.onBackground
                                )

                            }


                        }
                    }
                }


                Spacer(modifier = Modifier.padding(16.dp))


                DropDownMenuComponent(
                    error = serviceTypeError,
                    label = "Pick type",
                    expanded = expandDropDown1,
                    selectedOptionText = serviceType,
                    onExpandedChange = {
                        viewModel.onTriggerEvent(
                            BookAppointmentEvent.ServiceTypeDropDownExpandChange(
                                !expandDropDown1
                            )
                        )
                    },
                    onDismissRequest = {
                        viewModel.onTriggerEvent(
                            BookAppointmentEvent.ServiceTypeDropDownExpandChange(
                                false
                            )
                        )
                    }
                ) {
                    for (type in typesList) {

                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onClick = {
                                viewModel.onTriggerEvent(
                                    BookAppointmentEvent.ServiceTypeDropDownExpandChange(
                                        false
                                    )
                                )
                                viewModel.onTriggerEvent(
                                    BookAppointmentEvent.ServiceTypeChanged(
                                        type
                                    )
                                )
                            }) {
                            Column {

                                Text(
                                    text = type,
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onSurface
                                )

                                Divider(
                                    modifier = Modifier
                                        .padding(top = 8.dp, bottom = 8.dp)
                                        .fillMaxWidth()
                                        .height(.5.dp), color = MaterialTheme.colors.onBackground
                                )

                            }


                        }
                    }
                }







                Spacer(modifier = Modifier.padding(16.dp))


                Button(
                    contentPadding = PaddingValues(16.dp),
                    onClick = { viewModel.onTriggerEvent(BookAppointmentEvent.Submit) },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Book it !", style = MaterialTheme.typography.button)
                }

                Spacer(modifier = Modifier.padding(16.dp))

            }

        }
    }
}