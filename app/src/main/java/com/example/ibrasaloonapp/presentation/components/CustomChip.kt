package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.Orange

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomChip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {

    Chip(
        modifier = Modifier.padding(end = 8.dp),
        onClick = {
            onClick()
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = if (isSelected) Orange else MaterialTheme.colors.primaryVariant,
            contentColor = MaterialTheme.colors.onSurface
        )
    ) {
        Text(text = "${text}")
    }

}