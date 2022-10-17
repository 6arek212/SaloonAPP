package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.Black1
import com.example.ibrasaloonapp.presentation.theme.Gray1
import com.example.ibrasaloonapp.presentation.theme.Gray2


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppointmentOptionCard(
    text: String,
    icon: ImageVector = Icons.Outlined.CleaningServices,
    backgroundColor: Color = Gray2,
    textColor: Color = Black1,
    onClick: () -> Unit
) {

    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor
    ) {
        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = "")

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = text,
                    style = MaterialTheme.typography.body2,
                    color = textColor
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Divider(modifier = Modifier.fillMaxWidth(), color = Gray1, thickness = 2.dp)
        }
    }
}


@Composable
@Preview
fun AppointmentOptionCardPreview() {
    AppTheme {
        AppointmentOptionCard(text = "Mark As Done", onClick = {})
    }
}