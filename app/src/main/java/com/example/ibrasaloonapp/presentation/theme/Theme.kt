package com.example.ibrasaloonapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple,
    primaryVariant = PurpleVariant,
    onPrimary = Gray1,
    secondary = PurpleLite,
    onSecondary = Black1,
    error = Red,
    background = Gray2,
    onBackground = Black1,
    surface = Gray1,
    onSurface = textBlack
)

private val LightColorPalette = lightColors(
    primary = Black2,
    primaryVariant = Black4,
    onPrimary = Gray1,
    secondary = PurpleLite,
    onSecondary = Black1,
    error = Red,
    background = Gray1,
    onBackground = Black1,
    surface = White,
    onSurface = Black2,
    secondaryVariant = Black2
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = SFTypography,
        shapes = AppShapes,
        content = content
    )
}