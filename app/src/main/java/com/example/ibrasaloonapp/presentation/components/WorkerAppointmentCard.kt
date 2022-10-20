package com.example.ibrasaloonapp.presentation.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.TimePatterns
import com.example.ibrasaloonapp.core.navigateToWhatsapp
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.theme.*


private const val TAG = "AppointmentCard"

@Composable
fun WorkerAppointmentCard(
    modifier: Modifier = Modifier,
    appointment: Appointment,
    backgroundColor: Color = PurpleLite,
    statusBackgroundColor: Color = PurpleDark,
    textColor: Color = MaterialTheme.colors.onBackground,
    showOptionDialog: () -> Unit
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


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("ExampleScreen", "PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }

    Row(modifier = modifier.height(IntrinsicSize.Max)) {

        Row(modifier = Modifier.fillMaxHeight(.7f)) {

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



            Spacer(modifier = Modifier.padding(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = startTime, color = textColor, style = MaterialTheme.typography.body2
                )

                IconButton(onClick = showOptionDialog) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                        contentDescription = "edit"
                    )
                }

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
                                "free" -> Gray1
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
                        "free" -> stringResource(id = R.string.not_booked)
                        "hold" -> stringResource(id = R.string.held_by_worker)
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
                            text =
                            if (appointment.status == "hold") stringResource(R.string.held_by_worker)
                            else if (appointment.customer != null)
                                "${appointment.customer.firstName} ${appointment.customer.lastName}"
                            else stringResource(R.string.not_booked),
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

                        Spacer(modifier = Modifier.weight(1f))


                        if (appointment.customer != null) {

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                IconButton(onClick = {
                                    navigateToWhatsapp(context = context, phone = appointment.customer.phone)
                                }) {
                                    Image(
                                        modifier = Modifier.size(20.dp),
                                        painter = painterResource(id = R.drawable.whatsapp),
                                        contentDescription = "Whatsapp"
                                    )
                                }


                                IconButton(onClick = {

                                    when (PackageManager.PERMISSION_GRANTED) {
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.CALL_PHONE
                                        ) -> {
                                            // Some works that require permission
                                            val intent = Intent(Intent.ACTION_CALL)
                                            intent.data =
                                                Uri.parse("tel:${appointment.customer.phone}")
                                            startActivity(context, intent, null)
                                        }
                                        else -> {
                                            // Asking for permission
                                            launcher.launch(Manifest.permission.CALL_PHONE)
                                        }
                                    }

                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.call),
                                        contentDescription = "Call",
                                        tint = Gray1
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }


}


@Composable
@Preview
fun WorkerAppointmentCardPreview() {
    AppTheme() {

        WorkerAppointmentCard(
            appointment = Appointment(
                id = "1",
                startTime = "2022-06-10T13:00:00.00",
                endTime = "2022-06-10 13:00:00",
                status = "in-progress",
                customer = User("", "tarik", "husin", "0525145565", "barber"),
                worker = User("", "tarik", "husin", "0525145565", "barber"),
            ),
            showOptionDialog = {}
        )
    }
}
