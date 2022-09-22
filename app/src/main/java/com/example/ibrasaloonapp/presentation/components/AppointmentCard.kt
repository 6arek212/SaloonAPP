package com.example.ibrasaloonapp.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.TimePatterns
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.presentation.theme.*

private const val TAG = "AppointmentCard"

@Composable
fun AppointmentCard(
    modifier: Modifier = Modifier,
    appointment: Appointment,
    onUnbook: () -> Unit
) {


    Card(
        modifier = modifier,
        elevation = 2 .dp,
        backgroundColor = Gray1,
        shape = MaterialTheme.shapes.small
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(
                                when (appointment.status) {
                                    "in-progress" -> Orange
                                    "done" -> Green
                                    "didnt-come" -> Black4
                                    else -> Red
                                }
                            )
                            .width(15.dp)
                            .height(15.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(2.dp))

                Text(
                    text = when (appointment.status) {
                        "in-progress" -> stringResource(id = R.string.in_progress)
                        "done" -> stringResource(id = R.string.done)
                        "didnt-come" -> stringResource(id = R.string.didnt_attend)
                        else -> stringResource(id = R.string.canceled)
                    },
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
            }



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(
                    text = "${
                        stringDateFormat(
                            appointment.startTime,
                            TimePatterns.EEEE_MM_DD,
                            LocalContext.current
                        )
                    } " +
                            "${stringResource(id = R.string.at)} " +
                            stringDateFormat(
                                appointment.startTime,
                                TimePatterns.TIME_ONLY,
                                LocalContext.current
                            ),
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body1
                )


                Text(
                    text = "${stringResource(id = R.string.for_a)} ${appointment.service?.title}",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.padding(8.dp))


                Row() {
                    Text(
                        text = stringResource(id = R.string.with),
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )

                    ImageChip(
                        modifier = Modifier,
                        text = appointment.worker.firstName +
                                " ${appointment.worker.lastName}",
                        url = appointment.worker.image,
                        onClick = { /*TODO*/ },
                        isSelected = true
                    )
                }

            }


            if (appointment.status == "in-progress") {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onUnbook,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Red)

                ) {
                    Text(text = stringResource(id = R.string.unbook))
                }
            }
        }

    }

}