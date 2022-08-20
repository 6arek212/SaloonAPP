package com.example.ibrasaloonapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ibrasaloonapp.presentation.ui.add_appointment.AddAppointmentView
import com.example.ibrasaloonapp.presentation.ui.session_list.SessionListView
import com.example.ibrasaloonapp.presentation.ui.session_list.SessionListViewModel


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
        AddAppointmentView(navController = navController)
    }
}