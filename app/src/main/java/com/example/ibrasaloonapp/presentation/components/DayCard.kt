package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.GrayBlue
import com.example.ibrasaloonapp.presentation.theme.Orange
import com.example.ibrasaloonapp.presentation.theme.White


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DayCard(
    dayNumber: String,
    dayText: String,
    backgroundColor: Color = GrayBlue,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {

    Surface(
        modifier = Modifier
            .heightIn(min = 60.dp)
            .widthIn(min = 60.dp),
        color = if (isSelected) Orange else backgroundColor,
        shape = MaterialTheme.shapes.small,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = dayNumber, color = White, style = MaterialTheme.typography.body2)
            Text(text = dayText, color = White, style = MaterialTheme.typography.body2)
        }
    }


}


@Composable
@Preview
fun DayCardPreview() {
    AppTheme {
        DayCard("Mon", "06", onClick = {})
    }
}
