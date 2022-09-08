package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.Black1
import com.example.ibrasaloonapp.presentation.theme.Gray1
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.theme.Gray3

@Composable
fun ImageAndName(
    modifier: Modifier = Modifier,
    firstName: String = "Tarik",
    lastName: String = "Husin",
) {
    if (firstName.isBlank() || lastName.isBlank())
        throw Exception("First Name & Last Name cant be empty")

    Row(modifier = modifier) {

        Surface(
            modifier = Modifier
                .size(40.dp),
            elevation = 8.dp,
            color = Gray1,
            border = BorderStroke(2.dp, Gray2),
            shape = CircleShape
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${firstName.first()} ${lastName.first()}",
                    style = MaterialTheme.typography.h5,
                    color = Black1
                )
            }
        }


        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "Hello,",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = firstName,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = lastName,
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