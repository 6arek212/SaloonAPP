package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.presentation.theme.Gray1

@Composable
fun IconText(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    contentDescription: String,
    textStyle: TextStyle
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = MaterialTheme.shapes.small,
            color = Gray1
        ) {
            Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = icon,
                contentDescription = contentDescription,
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Text(text = text, style = textStyle)
    }
}