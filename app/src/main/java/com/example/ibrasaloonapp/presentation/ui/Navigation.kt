package com.example.ibrasaloonapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ibrasaloonapp.presentation.ui.book_appointment.BookAppointmentView
import com.example.ibrasaloonapp.presentation.ui.appointment_list.SessionListView


@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SessionList.route) {
        sessionList(navController = navController)
        addAppointment(navController = navController)
    }
}


fun NavGraphBuilder.sessionList(
    navController: NavController,
) {
    composable(
        route = Screen.SessionList.route,
        arguments = emptyList()
    ) {
        SessionListView(navController = navController)
    }
}


fun NavGraphBuilder.addAppointment(
    navController: NavController,
) {
    composable(
        route = Screen.AddAppointment.route,
        arguments = emptyList()
    ) {
        BookAppointmentView(navController = navController)
    }
}