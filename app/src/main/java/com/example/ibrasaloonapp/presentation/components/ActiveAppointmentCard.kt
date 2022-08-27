package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import com.example.ibrasaloonapp.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.presentation.theme.Green
import com.example.ibrasaloonapp.presentation.theme.Red
import com.example.ibrasaloonapp.presentation.theme.RedLite

@Composable
fun ActiveAppointmentCard(
    modifier: Modifier = Modifier,
    appointment: Appointment,
    onCancel : () -> Unit
) {

    Card(
        modifier = modifier,
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Green)
                            .width(15.dp)
                            .height(15.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(
                        text = "Nearest Appointment",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = appointment.type ?: "",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.padding(8.dp))

                DateTimeCard(
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .align(Alignment.CenterHorizontally),
                    date = appointment.date ?: "",
                    time = appointment.time ?: ""
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    painter = painterResource(id = R.drawable.barber_shop_brief),
                    contentDescription = ""
                )
            }


            Spacer(modifier = Modifier.padding(8.dp))


            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(.7f),
                    shape = RoundedCornerShape(bottomStart = 24.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = "Navigate",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                Button(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(bottomEnd = 24.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }

        }
    }


}