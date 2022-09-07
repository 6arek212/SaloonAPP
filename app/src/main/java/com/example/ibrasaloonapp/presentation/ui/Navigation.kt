package com.example.ibrasaloonapp.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ibrasaloonapp.domain.model.MenuItem
import com.example.ibrasaloonapp.presentation.ui.book_appointment.BookAppointmentView
import com.example.ibrasaloonapp.presentation.ui.appointment_list.AppointmentListView
import com.example.ibrasaloonapp.presentation.ui.home.DrawerBody
import com.example.ibrasaloonapp.presentation.ui.home.DrawerHeader
import com.example.ibrasaloonapp.presentation.ui.home.HomeView
import com.example.ibrasaloonapp.presentation.ui.login.LoginView
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileView
import com.example.ibrasaloonapp.presentation.ui.signup.SignupView
import com.example.ibrasaloonapp.presentation.ui.splash.SplashView
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerHeader()
            DrawerBody(items = drawerItems, onClick = { item ->
                when (item.id) {
                    Screen.Home.route -> {
                        navController.navigate(Screen.Home.route) {
                            launchSingleTop = true
                            popUpTo(0)
                        }
                    }
                    Screen.AppointmentsList.route -> {
                        navController.navigate(Screen.AppointmentsList.route) {
                            launchSingleTop = true
                            popUpTo(0)
                        }
                    }
                    Screen.Profile.route -> {
                        navController.navigate(Screen.Profile.route) {
                            launchSingleTop = true
                            popUpTo(0)
                        }
                    }

                    "logout" -> {
                        //remove auth data !!!
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            popUpTo(0)
                        }
                    }
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }
    ) {

        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Screen.AppointmentsList.route
        ) {
            splash(navController = navController)
            login(navController = navController)
            signup(navController = navController)
            home(navController = navController)
            appointmentList(navController = navController)
            bookAppointment(navController = navController)
            profile(navController = navController)
        }
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
        route = Screen.Signup.route,
        arguments = emptyList()
    ) {
        SignupView(navController = navController)
    }
}


fun NavGraphBuilder.home(
    navController: NavController,
) {
    composable(
        route = Screen.Home.route,
        arguments = emptyList()
    ) {
        HomeView(navController = navController)
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


fun NavGraphBuilder.profile(
    navController: NavController,
) {
    composable(
        route = Screen.Profile.route,
        arguments = emptyList()
    ) {
        ProfileView(navController = navController)
    }
}


val drawerItems = listOf(
    MenuItem(
        id = Screen.Home.route,
        title = "Home",
        contentDescription = "Go to home",
        icon = Icons.Filled.Home
    ),
    MenuItem(
        id = Screen.AppointmentsList.route,
        title = "Appointments",
        contentDescription = "Go to appointments",
        icon = Icons.Filled.BookOnline
    ),
    MenuItem(
        id = Screen.Profile.route,
        title = "Profile",
        contentDescription = "Go to profile",
        icon = Icons.Filled.AccountBox
    ),
    MenuItem(
        id = "logout",
        title = "Logout",
        contentDescription = "logout",
        icon = Icons.Filled.ExitToApp
    )
)