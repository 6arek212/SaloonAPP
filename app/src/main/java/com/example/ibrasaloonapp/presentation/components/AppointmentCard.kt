package com.example.ibrasaloonapp.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.stringDateToDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.presentation.theme.Green
import com.example.ibrasaloonapp.presentation.theme.Red

private const val TAG = "AppointmentCard"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppointmentCard(
    modifier: Modifier = Modifier,
    appointment: Appointment,
    onUnbook: () -> Unit
) {


    Card(
        modifier = modifier,
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.small
    ) {
        ConstraintLayout() {
            val (active, text, button) = createRefs()


            Box(modifier = Modifier.constrainAs(active) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }) {
                if (appointment.isActive) {
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
            }


            Column(modifier = Modifier.padding(16.dp).constrainAs(text) {
                start.linkTo(active.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }) {
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



            Button(
                modifier = Modifier.constrainAs(button) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(text.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
                onClick = onUnbook,
                colors = ButtonDefaults.buttonColors(backgroundColor = Red)

            ) {
                Text(text = "unbook")
            }

        }

    }

}