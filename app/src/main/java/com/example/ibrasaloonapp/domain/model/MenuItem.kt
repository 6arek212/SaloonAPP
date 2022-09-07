package com.example.ibrasaloonapp.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val contentDescription: String
) {
}