package com.example.ibrasaloonapp.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.TimePatterns
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.theme.*

private const val TAG = "AppointmentCard"

@Composable
fun AppointmentCard2(
    modifier: Modifier = Modifier,
    appointment: Appointment,
    backgroundColor: Color = PurpleLite,
    statusBackgroundColor: Color = PurpleDark,
    textColor: Color = MaterialTheme.colors.onBackground,
    showTimeLine: Boolean = false,
    onUnbook: () -> Unit
) {

    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var fullTime by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = appointment) {
        startTime = stringDateFormat(
            appointment.startTime,
            TimePatterns.TIME_ONLY,
            context
        )

        endTime = stringDateFormat(
            appointment.endTime,
            TimePatterns.TIME_ONLY,
            context
        )

        fullTime = stringDateFormat(
            appointment.startTime,
            TimePatterns.EEEE_MM_DD,
            context
        )
    }



    Row(modifier = modifier.height(IntrinsicSize.Max)) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(16.dp)
                    .background(backgroundColor)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight(.5f)
                    .width(4.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(backgroundColor)
            )



            Spacer(modifier = Modifier.padding(2.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(12.dp)
                    .width(4.dp)
                    .background(backgroundColor)
            )


            Spacer(modifier = Modifier.padding(4.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(8.dp)
                    .width(4.dp)
                    .background(backgroundColor)
            )


            Spacer(modifier = Modifier.padding(4.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(6.dp)
                    .width(4.dp)
                    .background(backgroundColor)
            )
        }



        if (showTimeLine) {
            Spacer(modifier = Modifier.padding(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = startTime, color = textColor, style = MaterialTheme.typography.body2
                )

                Text(
                    text = endTime, color = textColor, style = MaterialTheme.typography.body2
                )
            }
        }

        Spacer(modifier = Modifier.padding(4.dp))



        Column {

            Row(
                modifier = Modifier
                    .offset(y = 16.dp)
                    .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                    .background(statusBackgroundColor)
                    .padding(bottom = 20.dp, start = 8.dp, end = 8.dp, top = 8.dp)
                    .align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            when (appointment.status) {
                                "in-progress" -> Orange
                                "done" -> Green
                                "didnt-come" -> Black4
                                else -> Red
                            }
                        )
                )

                Spacer(modifier = Modifier.padding(6.dp))

                Text(
                    text = when (appointment.status) {
                        "in-progress" -> stringResource(id = R.string.in_progress)
                        "done" -> stringResource(id = R.string.done)
                        "didnt-come" -> stringResource(id = R.string.didnt_attend)
                        else -> stringResource(id = R.string.canceled)
                    }, color = White, style = MaterialTheme.typography.body2
                )
            }


            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = backgroundColor,
                elevation = 10.dp,
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.personalcard),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.padding(4.dp))

                        Text(
                            text = "${appointment.customer?.firstName} ${appointment.customer?.lastName}",
                            color = White,
                            style = MaterialTheme.typography.h5
                        )
                    }


                    Text(
                        text = "${startTime} - ${endTime}",
                        color = White,
                        style = MaterialTheme.typography.h5
                    )

                    Text(
                        text = fullTime,
                        color = White,
                        style = MaterialTheme.typography.body2
                    )

                    Spacer(modifier = Modifier.padding(12.dp))


                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.scissor),
                            contentDescription = "scissor",
                            tint = Gray1
                        )


                        ImageChip(
                            modifier = Modifier,
                            text = appointment.worker.firstName +
                                    " ${appointment.worker.lastName}",
                            url = appointment.worker.image,
                            onClick = { /*TODO*/ },
                            isSelected = false,
                            imageSize = 36.dp,
                            chipPadding = PaddingValues(horizontal = 24.dp, vertical = 2.dp)
                        )
                    }


                    if (appointment.status == "in-progress") {
                        Spacer(modifier = Modifier.padding(6.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onUnbook,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Red)
                        ) {
                            Text(
                                text = stringResource(id = R.string.unbook),
                                color = White,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }
        }
    }


}


@Composable
@Preview
fun AppointmentCardPreview() {
    AppTheme() {

        AppointmentCard2(
            appointment = Appointment(
                id = "1",
                startTime = "2022-06-10T13:00:00.00",
                endTime = "2022-06-10 13:00:00",
                status = "in-progress",
                customer = User("", "tarik", "husin", "0525145565", "barber"),
                worker = User("", "tarik", "husin", "0525145565", "barber"),
            ),
            onUnbook = {}
        )
    }
}
