package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.Black2


sealed class UpdateAppointmentEvent {
    class UpdateStatus(val status: String, val service: String? = null) : UpdateAppointmentEvent()
    object DeleteAppointment : UpdateAppointmentEvent()
}


@Composable
fun UpdateAppointmentDialog(onUpdate: (UpdateAppointmentEvent) -> Unit) {

    Surface(shape = MaterialTheme.shapes.large) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                TextButton(onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("done")) }) {
                    Text(text = "Done", style = MaterialTheme.typography.body2, color = Black2)
                }
            }
            Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                TextButton(onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("canceled")) }) {
                    Text(text = "Canceled", style = MaterialTheme.typography.body2, color = Black2)
                }
            }
            Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                TextButton(onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("didnt-come")) }) {
                    Text(
                        text = "Customer didnt come",
                        style = MaterialTheme.typography.body2,
                        color = Black2
                    )
                }
            }
            Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                TextButton(onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("in-progress")) }) {
                    Text(
                        text = "In progress",
                        style = MaterialTheme.typography.body2,
                        color = Black2
                    )
                }
            }
            Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                TextButton(onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("free")) }) {
                    Text(text = "Free", style = MaterialTheme.typography.body2, color = Black2)
                }
            }
            Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {

                TextButton(onClick = { onUpdate(UpdateAppointmentEvent.UpdateStatus("hold")) }) {
                    Text(text = "Hold", style = MaterialTheme.typography.body2, color = Black2)
                }
            }

            Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {

                TextButton(onClick = { onUpdate(UpdateAppointmentEvent.DeleteAppointment) }) {
                    Text(
                        text = "Delete Appointment",
                        style = MaterialTheme.typography.body2,
                        color = Black2
                    )
                }
            }
        }
    }

}


@Composable
@Preview
fun UpdateAppointmentPreview() {
    AppTheme {
        UpdateAppointmentDialog(onUpdate = {})
    }
}