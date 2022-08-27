package com.example.ibrasaloonapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ibrasaloonapp.presentation.ui.book_appointment.BookAppointmentView
import com.example.ibrasaloonapp.presentation.ui.appointment_list.AppointmentListView
import com.example.ibrasaloonapp.presentation.ui.login.LoginView
import com.example.ibrasaloonapp.presentation.ui.signup.SignupView
import com.example.ibrasaloonapp.presentation.ui.splash.SplashView


@Composable
fun Navigation(firstRoute: String = Screen.Splash.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.AppointmentsList.route) {
        splash(navController = navController)
        login(navController = navController)
        signup(navController = navController)
        appointmentList(navController = navController)
        bookAppointment(navController = navController)
    }
}

fun NavGraphBuilder.splash(
    navController: NavController,
) {
    composable(
        route = Screen.Splash.route,
        arguments = emptyList()
    ) {
        SplashView(navController = navController)
    }
}

fun NavGraphBuilder.login(
    navController: NavController,
) {
    composable(
        route = Screen.Login.route,
        arguments = emptyList()
    ) {
        LoginView(navController = navController)
    }
}


fun NavGraphBuilder.signup(
    navController: NavController,
) {
    composable(
        route = Screen.Login.route,
        arguments = emptyList()
    ) {
        SignupView(navController = navController)
    }
}


fun NavGraphBuilder.appointmentList(
    navController: NavController,
) {
    composable(
        route = Screen.AppointmentsList.route,
        arguments = emptyList()
    ) {
        AppointmentListView(navController = navController)
    }
}


fun NavGraphBuilder.bookAppointment(
    navController: NavController,
) {
    composable(
        route = Screen.BookAppointment.route,
        arguments = emptyList()
    ) {
        BookAppointmentView(navController = navController)
    }
}