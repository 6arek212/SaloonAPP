package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.stringDateToDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment


@Composable
fun AppointmentCard(
    modifier: Modifier = Modifier,
    appointment: Appointment
) {
    Card(
        modifier = modifier.heightIn(min = 100.dp),
        elevation = 1.dp,
        backgroundColor = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium
    ) {


        Column(modifier = Modifier.padding(16.dp)) {

            Image(
                painter = painterResource(id = R.drawable.tools2),
                contentDescription = "",
                modifier = Modifier
                    .width(40.dp)
                    .align(CenterHorizontally)
            )


            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = "${appointment.service} - Done By " +
                        "${appointment.worker.firstName}" +
                        " ${appointment.worker.lastName}",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )


            Text(
                text = stringDateToDateFormat(appointment.startTime),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }


    }
}