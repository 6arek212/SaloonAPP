package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
        elevation = 8.dp,
        backgroundColor = Gray1,
        shape = MaterialTheme.shapes.small
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (appointment.isActive) {
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
                                .background(Green)
                                .width(15.dp)
                                .height(15.dp)
                        )
                        Spacer(modifier = Modifier.padding(16.dp))
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        text = stringResource(id = R.string.active_appointment),
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "${appointment.service}",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )



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




                Spacer(modifier = Modifier.padding(8.dp))


                ImageChip(
                    modifier = Modifier,
                    text = appointment.worker.firstName +
                            " ${appointment.worker.lastName}",
                    url = appointment.worker.image,
                    onClick = { /*TODO*/ },
                    isSelected = true
                )
            }


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