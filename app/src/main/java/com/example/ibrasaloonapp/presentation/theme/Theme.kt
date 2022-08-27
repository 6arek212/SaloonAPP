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
    onPrimary = Gray,
    secondary = PurpleLite,
    onSecondary = Black1,
    error = Red,
    background = GrayBackground,
    onBackground = Black1,
    surface = Gray,
    onSurface = textBlack
)

private val LightColorPalette = lightColors(
    primary = Purple,
    primaryVariant = PurpleVariant,
    onPrimary = Gray,
    secondary = PurpleLite,
    onSecondary = Black1,
    error = Red,
    background = GrayBackground,
    onBackground = Black1,
    surface = Gray,
    onSurface = Black2
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