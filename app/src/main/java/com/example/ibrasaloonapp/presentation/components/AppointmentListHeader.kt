package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.ui.Screen

@Composable
fun SessionListHeader(
    navigateToAddAppointment: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .background(MaterialTheme.colors.surface)
    ) {

        Image(
            modifier = Modifier.fillMaxHeight(),
            painter = painterResource(id = R.drawable.barber_shop_brief),
            contentDescription = ""
        )


        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = navigateToAddAppointment
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = "Book An Appointment",
                style = MaterialTheme.typography.h5
            )
        }
    }
}