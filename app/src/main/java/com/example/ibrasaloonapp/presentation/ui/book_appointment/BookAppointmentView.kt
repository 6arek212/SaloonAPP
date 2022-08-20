package com.example.ibrasaloonapp.presentation.ui.book_appointment

import DropDownMenuComponent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.components.DatePicker
import com.example.ibrasaloonapp.presentation.theme.PurpleGrey

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookAppointmentView(
    navController: NavController,
    viewModel: BookAppointmentViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

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
                    .height(.5.dp), color = PurpleGrey
            )

            Spacer(modifier = Modifier.padding(16.dp))



            DatePicker(
                error = state.dateError,
                label = "Pick Date",
                datePicked = state.date,
                updatedDate = { updatedDate ->
                    viewModel.onTriggerEvent(BookAppointmentEvent.DateChanged(updatedDate))
                })

            Spacer(modifier = Modifier.padding(16.dp))


            DropDownMenuComponent(
                error = state.timeError,
                label = "Pick time",
                expanded = state.expandDropDown2,
                selectedOptionText = state.time,
                onExpandedChange = {
                    viewModel.onTriggerEvent(BookAppointmentEvent.TimeDropDownExpandChange(!state.expandDropDown2))
                },
                onDismissRequest = {
                    viewModel.onTriggerEvent(BookAppointmentEvent.TimeDropDownExpandChange(false))
                }
            ) {
                for (time in state.appointmentsList) {

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
                                    .height(.5.dp), color = PurpleGrey
                            )

                        }


                    }
                }
            }


            Spacer(modifier = Modifier.padding(16.dp))


            DropDownMenuComponent(
                error = state.serviceTypeError,
                label = "Pick type",
                expanded = state.expandDropDown1,
                selectedOptionText = state.serviceType,
                onExpandedChange = {
                    viewModel.onTriggerEvent(
                        BookAppointmentEvent.ServiceTypeDropDownExpandChange(
                            !state.expandDropDown1
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
                for (type in state.typesList) {

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
                            viewModel.onTriggerEvent(BookAppointmentEvent.ServiceTypeChanged(type))
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
                                    .height(.5.dp), color = PurpleGrey
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