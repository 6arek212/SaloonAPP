package com.example.ibrasaloonapp.presentation.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument>) {

    object Splash : Screen(
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

    object Home : Screen(
        route = "Home",
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



    object UploadImage : Screen(
        route = "UploadImage",
        arguments = emptyList()
    )

    object ProfileRoot : Screen(
        route = "ProfileRoot",
        arguments = emptyList()
    )

    object Profile : Screen(
        route = "Profile",
        arguments = emptyList()
    )

    object EditProfile : Screen(
        route = "EditProfile",
        arguments = listOf(
            navArgument("userId") {
                type = NavType.StringType
            },
            navArgument("firstName") {
                type = NavType.StringType
            },
            navArgument("lastName") {
                type = NavType.StringType
            },
            navArgument("phone") {
                type = NavType.StringType
            }
        )
    )


    object UpdatePhoneNumber : Screen(
        route = "UpdatePhoneNumber",
        arguments = emptyList()
    )

}