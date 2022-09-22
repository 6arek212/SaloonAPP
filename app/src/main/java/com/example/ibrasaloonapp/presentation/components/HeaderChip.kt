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


@Composable
fun HeaderChip() {

    Column(modifier = Modifier) {
        CustomChip(text = "Hair Cut", onClick = { /*TODO*/ }, isSelected = false)
        Card(
            modifier = Modifier.offset(y = -0.dp),
            elevation = 8.dp,
            shape = MaterialTheme.shapes.large,

        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = "50₪", style = MaterialTheme.typography.body2)
            }
        }

    }


}


@Preview
@Composable
fun Chip() {
    AppTheme {
        HeaderChip()
    }

}