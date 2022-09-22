package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomChip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    backgroundColor: Color = Gray1,
    contentPaddingValues: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 8.dp)
) {

    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .widthIn(min = 70.dp)
        ,
        elevation = if (isSelected) 4.dp else 0.dp,
        backgroundColor = if (isSelected) Orange else backgroundColor,
        onClick = onClick,
        shape = MaterialTheme.shapes.large,

        ) {
        Box(
            modifier = Modifier
//                .padding(contentPaddingValues)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
                style = MaterialTheme.typography.body2,
                color = if (isSelected) Gray1 else Black2
            )
        }
    }

}