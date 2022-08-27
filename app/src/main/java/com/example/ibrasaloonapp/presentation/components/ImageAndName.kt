package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.Purple

@Composable
fun ImageAndName(
    modifier: Modifier = Modifier,
    firstName: String = "Tarik",
    lastName: String = "Husin",
) {
    if (firstName.isBlank() || lastName.isBlank())
        throw Exception("First Name & Last Name cant be empty")

    Row(modifier = modifier) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primaryVariant)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${firstName.first()} ${lastName.first()}",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onPrimary
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "Hello",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "${firstName} ${lastName}",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onPrimary
        )
    }
}


@Preview(showBackground = false)
@Composable
fun Prv() {
    ImageAndName(Modifier.fillMaxWidth())
}