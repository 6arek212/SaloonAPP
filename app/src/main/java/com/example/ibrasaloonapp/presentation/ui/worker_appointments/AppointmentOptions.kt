package com.example.ibrasaloonapp.presentation.ui.services

import DropDownMenuComponent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.Service
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.worker_appointments.UpdateAppointmentEvent


@Composable
fun AppointmentOptions(
    appointment: Appointment?,
    services: List<Service>,
    onUpdate: (UpdateAppointmentEvent) -> Unit
) {
    var showHoldAppointment by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .background(Gray2)
            .padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(6.dp)
                .clip(MaterialTheme.shapes.large)
                .background(Gray3)
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = stringResource(R.string.appointment_options),
            style = MaterialTheme.typography.h4
        )

        Spacer(modifier = Modifier.padding(8.dp))

        if (!showHoldAppointment) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {

                    item {
                        AppointmentOptionCard(
                            icon = Icons.Outlined.Done,
                            text = stringResource(id = R.string.done),
                            onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("done")) })
                    }

                    if (appointment?.status != "hold")
                        item {
                            AppointmentOptionCard(
                                icon = Icons.Outlined.WorkOutline,
                                text = stringResource(id = R.string.in_progress),
                                onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("in-progress")) })
                        }

                    item {
                        AppointmentOptionCard(
                            icon = Icons.Outlined.Cancel,
                            text = stringResource(id = R.string.canceled),
                            onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("canceled")) })
                    }

                    item {
                        AppointmentOptionCard(
                            icon = Icons.Outlined.NordicWalking,
                            text = stringResource(id = R.string.didnt_attend),
                            onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("didnt-come")) })
                    }

                    item {
                        AppointmentOptionCard(
                            icon = Icons.Outlined.BookmarkAdded,
                            text = stringResource(id = R.string.hold),
                            onClick = { showHoldAppointment = true })
                    }

                    item {
                        AppointmentOptionCard(
                            icon = Icons.Outlined.CropFree,
                            text = stringResource(id = R.string.free),
                            onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("free")) })
                    }

                    item {
                        AppointmentOptionCard(
                            icon = Icons.Outlined.Delete,
                            backgroundColor = Red,
                            textColor = White,
                            text = stringResource(R.string.delete_appointment),
                            onClick = { onUpdate(UpdateAppointmentEvent.DeleteAppointment) })
                    }
                }

            }
        } else {
            HoldAppointment(
                services = services,
                dismiss = { showHoldAppointment = false },
                onHoldAppointment = {
                    onUpdate(UpdateAppointmentEvent.UpdateStatus(status = "hold", service = it))
                    showHoldAppointment = false
                })
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HoldAppointment(
    services: List<Service>,
    dismiss: () -> Unit,
    onHoldAppointment: (service: String) -> Unit
) {
    var dropDownExpand by rememberSaveable {
        mutableStateOf(false)
    }

    var ketTitle by rememberSaveable {
        mutableStateOf("")
    }

    var title by rememberSaveable {
        mutableStateOf("")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BackButton(modifier = Modifier.align(Alignment.Start), onClick = dismiss, tint = Black1)

        DropDownMenuComponent(
            label = stringResource(id = R.string.pick_service),
            expanded = dropDownExpand,
            selectedOptionText = title,
            onExpandedChange = { dropDownExpand = !dropDownExpand },
            onDismissRequest = { dropDownExpand = false }) {

            services.forEach { service ->
                DropdownMenuItem(onClick = {
                    ketTitle = service.titleKey
                    title = service.title
                    dropDownExpand = false
                }) {
                    Text(text = service.title)
                }
            }
        }


        Spacer(modifier = Modifier.padding(12.dp))

        ProgressButton(
            onClick = {
                onHoldAppointment(ketTitle)
            },
            color = MaterialTheme.colors.primary,
            progressColor = MaterialTheme.colors.onPrimary
        ) {
            Text(text = stringResource(id = R.string.hold), style = MaterialTheme.typography.body2)
        }

        Spacer(modifier = Modifier.padding(4.dp))
    }

}


@Composable
@Preview
fun AppointmentOptionsPreview() {
    AppTheme {
        AppointmentOptions(appointment = null, services = listOf(), onUpdate = {})
    }
}