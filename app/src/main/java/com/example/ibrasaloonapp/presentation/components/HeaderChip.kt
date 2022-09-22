package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.Orange
import com.example.ibrasaloonapp.presentation.theme.Orange2


@Composable
fun HeaderChip(
    text: String,
    headerText: String,
    onClick: () -> Unit,
    isSelected: Boolean,
) {

    Column(modifier = Modifier) {
        CustomChip(text = text, onClick = onClick, isSelected = isSelected)
        Card(
            modifier = Modifier.offset(y = -10.dp),
            elevation = 8.dp,
            backgroundColor = Orange2,
            shape = MaterialTheme.shapes.large
        ) {
            Box(modifier = Modifier
                .padding(vertical = 4.dp , horizontal = 8.dp)) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.background
                )
            }
        }
    }
}


@Preview
@Composable
fun Chip() {
    AppTheme {
        HeaderChip(
            text = "Hair Cut",
            onClick = { },
            isSelected = false,
            headerText = "50₪"
        )
    }

}