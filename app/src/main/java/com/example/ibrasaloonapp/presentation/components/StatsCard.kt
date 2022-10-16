package com.example.ibrasaloonapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibrasaloonapp.presentation.theme.GrayBlue
import com.example.ibrasaloonapp.presentation.theme.White


@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    visible: Boolean,
    leadingIcon: ImageVector,
    contentDescription: String = title,
    textColor: Color = White,
    cardColor: Color = GrayBlue,
) {

    AnimatedVisibility(
        modifier = modifier,
        visible = visible
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(cardColor)
                .padding(16.dp)
        ) {
            Icon(imageVector = leadingIcon, contentDescription = contentDescription)

            Spacer(modifier = Modifier.padding(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, color = textColor, style = MaterialTheme.typography.h6)
                Text(text = value, color = textColor, style = MaterialTheme.typography.h4 )
            }
        }

    }
}


@Composable
@Preview
fun StatsCardPreview() {

//    StatsCard(title = "Count", value = "545", visible = true)

}