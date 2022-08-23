package com.example.ibrasaloonapp.presentation.ui

import androidx.navigation.NamedNavArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument>) {

    object Splash: Screen(
        route = "Splash",
        arguments = emptyList()
    )

    object Login : Screen(
        route = "Login",
        arguments = emptyList()
    )

    object Signup : Screen(
        route = "Signup",
        arguments = emptyList()
    )

    object AppointmentsList : Screen(
        route = "AppointmentsList",
        arguments = emptyList()
    )

    object BookAppointment : Screen(
        route = "BookAppointment",
        arguments = emptyList()
    )



}