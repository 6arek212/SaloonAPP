package com.example.ibrasaloonapp.presentation.ui

import androidx.navigation.NamedNavArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument>) {

    object Splash: Screen(
        route = "Splash",
        arguments = emptyList()
    )

    object SessionList : Screen(
        route = "SessionList",
        arguments = emptyList()
    )

    object AddAppointment : Screen(
        route = "AddAppointment",
        arguments = emptyList()
    )

}