package com.example.ibrasaloonapp.presentation.ui.worker_appointments

import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.KeyValueWrapper
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import java.util.*

fun addPrecedingZero(number: Int): String {
    return if (number <= 9) "0$number" else number.toString()
}


@Composable
fun CreateAppointmentView(viewModel: WorkerAppointmentsListViewModel) {
    val context = LocalContext.current
    val progress = viewModel.uiState.collectAsState().value.progressBarState
    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage
    val mCalendar = Calendar.getInstance()
    var showStatusMenu by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (showStatusMenu)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown


    var isRangeAppointment by rememberSaveable {
        mutableStateOf(false)
    }

    var interval by rememberSaveable {
        mutableStateOf("")
    }

    var startHour by rememberSaveable {
        mutableStateOf("")
    }

    var startMin by rememberSaveable {
        mutableStateOf("")
    }

    var endHour by rememberSaveable {
        mutableStateOf("")
    }

    var endMin by rememberSaveable {
        mutableStateOf("")
    }

    var statusSelected by rememberSaveable {
        mutableStateOf(KeyValueWrapper("free", context.getString(R.string.free)))
    }

    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]


    // Creating a TimePicker dialog
    val startTimePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hour: Int, min: Int ->
            startHour = addPrecedingZero(hour)
            startMin = addPrecedingZero(min)
        }, mHour, mMinute, true
    )


    val endTimePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hour: Int, min: Int ->
            endHour = addPrecedingZero(hour)
            endMin = addPrecedingZero(min)
        }, mHour, mMinute, true
    )


    DefaultScreenUI(
        uiComponent = uiMessage,
        progressBarState = progress,
        onRemoveUIComponent = { viewModel.onTriggerEvent(WorkerAppointmentsListEvent.DismissUIMessage) }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(shape = MaterialTheme.shapes.large) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    SubTitle(
                        modifier = Modifier.align(Alignment.Start),
                        text = stringResource(R.string.create_appointment)
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(8.dp)
                    )


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(
                            6.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isRangeAppointment,
                                onClick = { isRangeAppointment = true })
                            Text(
                                text = stringResource(R.string.range),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = !isRangeAppointment,
                                onClick = { isRangeAppointment = false })
                            Text(
                                text = stringResource(R.string.single),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }


                    Spacer(
                        modifier = Modifier
                            .padding(4.dp)
                    )

                    OutlinedTextField(
                        label = {
                            Text(text = stringResource(R.string.start_time))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { s ->
                        },
                        value = "$startHour:$startMin",
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Filled.AccessTime, stringResource(R.string.start_time))
                        },
                        textStyle = MaterialTheme.typography.body2,
                        modifier = Modifier.clickable { startTimePickerDialog.show() },
                        readOnly = true,
                        enabled = false
                    )


                    Spacer(modifier = Modifier.padding(4.dp))

                    OutlinedTextField(
                        label = {
                            Text(text = stringResource(R.string.end_time))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { s ->
                        },
                        value = "$endHour:$endMin",
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Filled.Timelapse, stringResource(R.string.end_time))
                        },
                        textStyle = MaterialTheme.typography.body2,
                        modifier = Modifier.clickable { endTimePickerDialog.show() },
                        readOnly = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    AnimatedVisibility(visible = isRangeAppointment) {
                        Column {

                            OutlinedTextField(
                                label = {
                                    Text(text = stringResource(R.string.interval))
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                onValueChange = { s ->
                                    interval = s
                                },
                                value = interval,
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Filled.Timer, stringResource(R.string.interval))
                                },
                                textStyle = MaterialTheme.typography.body2,
                            )

                            Spacer(modifier = Modifier.padding(4.dp))
                        }
                    }


                    Box {

                        OutlinedTextField(
                            label = {
                                Text(text = stringResource(R.string.status))
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            onValueChange = { s ->
                            },
                            value = statusSelected.value,
                            singleLine = true,
                            trailingIcon = {
                                Icon(icon, "contentDescription",
                                    Modifier.clickable { showStatusMenu = !showStatusMenu })
                            },
                            textStyle = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    //This value is used to assign to the DropDown the same width
                                    textFieldSize = coordinates.size.toSize()
                                }
                                .clickable { showStatusMenu = !showStatusMenu },
                            readOnly = true,
                            enabled = false
                        )


                        DropdownMenu(
                            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                            expanded = showStatusMenu,
                            onDismissRequest = { showStatusMenu = false }) {
                            DropdownMenuItem(onClick = {
                                statusSelected =
                                    KeyValueWrapper("free", context.getString(R.string.free))
                                showStatusMenu = false
                            }) {
                                Text(
                                    text = stringResource(R.string.free),
                                    style = MaterialTheme.typography.body2
                                )
                            }

                            DropdownMenuItem(onClick = {
                                statusSelected =
                                    KeyValueWrapper("hold", context.getString(R.string.hold))
                                showStatusMenu = false
                            }) {
                                Text(
                                    text = stringResource(R.string.hold),
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(16.dp))


                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (isRangeAppointment) {
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.CreateRangeAppointments(
                                        startHour = startHour,
                                        startMin = startMin,
                                        endHour = endHour,
                                        endMin = endMin,
                                        status = statusSelected.key,
                                        interval = interval
                                    )
                                )
                            } else {
                                viewModel.onTriggerEvent(
                                    WorkerAppointmentsListEvent.CreateAppointment(
                                        startHour = startHour,
                                        startMin = startMin,
                                        endHour = endHour,
                                        endMin = endMin,
                                        status = statusSelected.key
                                    )
                                )
                            }

                        }) {
                        Text(
                            text = stringResource(R.string.create),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }

}


@Composable
@Preview
fun CreateAppointmentPreview() {
    AppTheme {
//        CreateAppointmentView()
    }
}


