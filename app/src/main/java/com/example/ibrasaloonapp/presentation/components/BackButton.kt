package com.example.ibrasaloonapp.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ibrasaloonapp.presentation.theme.White


@Composable
fun BackButton(modifier: Modifier = Modifier, tint: Color = White, onClick: () -> Unit) {

    IconButton(modifier = modifier, onClick = onClick) {
        if (LocalLayoutDirection.current == LayoutDirection.Ltr) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back", tint = tint)
        } else {
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "back", tint = tint)
        }
    }

}