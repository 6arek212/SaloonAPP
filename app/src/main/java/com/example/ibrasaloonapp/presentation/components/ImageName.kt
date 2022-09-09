package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.theme.Black1
import com.example.ibrasaloonapp.presentation.theme.Gray1
import com.example.ibrasaloonapp.presentation.theme.Gray2

@Composable
fun ImageName(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    leadingText: String = stringResource(id = R.string.hello)
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
            text = "${leadingText}, ${firstName} ${lastName}",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onPrimary
        )
    }
}

