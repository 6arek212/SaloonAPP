package com.example.ibrasaloonapp.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.MenuItem
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.theme.Gray1
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.ui.book_appointment.BookAppointmentView
import com.example.ibrasaloonapp.presentation.ui.appointment_list.AppointmentListView
import com.example.ibrasaloonapp.presentation.ui.book_appointment.APPOINTMENT_KEY
import com.example.ibrasaloonapp.presentation.ui.edit_profile.EditProfileView
import com.example.ibrasaloonapp.presentation.ui.edit_profile.USER_KEY
import com.example.ibrasaloonapp.presentation.ui.home.*
import com.example.ibrasaloonapp.presentation.ui.login.LoginView
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileEvent
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileView
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileViewModel
import com.example.ibrasaloonapp.presentation.ui.signup.SignupView
import com.example.ibrasaloonapp.presentation.ui.splash.SplashView
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "Navigation"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(mainViewModel: MainActivityViewModel) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val user = mainViewModel.state.value.authData?.user


    val events = mainViewModel.uiEvents

    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is MainUIEvent.Logout -> {

                        val currentRout = navController.currentDestination?.route
                        Log.d(TAG, "Navigation: ${currentRout}")
                        currentRout?.let {
                            if (currentRout != Screen.Home.route && currentRout != Screen.Signup.route) {
                                navController.navigate(Screen.Home.route) {
                                    launchSingleTop = true
                                    popUpTo(0)
                                }
                            }
                        }
                    }

                }
            }
        }
    }


    val drawerItems = listOf(
        MenuItem(
            id = Screen.Home.route,
            title = stringResource(id = R.string.home),
            contentDescription = "Go to home",
            icon = Icons.Filled.Home
        ),
        MenuItem(
            id = Screen.AppointmentsList.route,
            title = stringResource(id = R.string.appointments),
            contentDescription = "Go to appointments",
            icon = Icons.Filled.BookOnline
        ),
        MenuItem(
            id = Screen.Profile.route,
            title = stringResource(id = R.string.profile),
            contentDescription = "Go to profile",
            icon = Icons.Filled.AccountBox
        ),
        MenuItem(
            id = "logout",
            title = stringResource(id = R.string.logut),
            contentDescription = "logout",
            icon = Icons.Filled.ExitToApp
        )
    )




    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        drawerBackgroundColor = Gray2,
        drawerGesturesEnabled = user != null,
        drawerContent =
        {
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
                        scope.launch { scaffoldState.drawerState.close() }
                        mainViewModel.onTriggerEvent(MainEvent.Logout)
                    }
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }, itemTextStyle = MaterialTheme.typography.body2)
        }
    ) {

        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Screen.Signup.route
        ) {
            splash(navController = navController)
            login(navController = navController)
            signup(navController = navController, mainViewModel = mainViewModel)
            home(navController = navController, mainViewModel = mainViewModel)
            appointmentList(navController = navController, mainViewModel = mainViewModel)
            bookAppointment(navController = navController, mainViewModel = mainViewModel)
            profile(navController = navController, mainViewModel = mainViewModel)
            editProfile(navController = navController, mainViewModel = mainViewModel)
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
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.Signup.route,
        arguments = emptyList()
    ) {
        SignupView(navController = navController, mainViewModel = mainViewModel)
    }
}


fun NavGraphBuilder.home(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.Home.route,
        arguments = emptyList()
    ) { backStackEntry ->

        val viewModel: HomeViewModel = hiltViewModel()

        val appointment = backStackEntry
            .savedStateHandle
            .getLiveData<Appointment>(APPOINTMENT_KEY)
            .observeAsState().value


        appointment?.let {
            viewModel.onTriggerEvent(HomeEvent.UpdateAppointment(appointment))
        }

        HomeView(
            navController = navController,
            viewModel = viewModel,
            mainViewModel = mainViewModel
        )
    }
}


fun NavGraphBuilder.appointmentList(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.AppointmentsList.route,
        arguments = emptyList()
    ) {
        AppointmentListView(navController = navController, mainViewModel = mainViewModel)
    }
}


fun NavGraphBuilder.bookAppointment(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.BookAppointment.route,
        arguments = emptyList()
    ) {
        BookAppointmentView(
            navController = navController,
            popBackStack = { appointment ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(APPOINTMENT_KEY, appointment)
            }, mainViewModel = mainViewModel
        )
    }
}


fun NavGraphBuilder.profile(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.Profile.route,
        arguments = emptyList(),
    ) { backStackEntry ->

        val user = backStackEntry
            .savedStateHandle
            .getLiveData<User>(USER_KEY)
            .observeAsState().value

        val viewModel: ProfileViewModel = hiltViewModel()

        user?.let {
            viewModel.onTriggerEvent(ProfileEvent.UpdateUser(user))
        }

        ProfileView(
            navController = navController,
            viewModel = viewModel,
            mainViewModel = mainViewModel
        )
    }
}

fun NavGraphBuilder.editProfile(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.EditProfile.route + "/{userId}/{firstName}/{lastName}/{phone}",
        arguments = Screen.EditProfile.arguments
    ) {
        val previousViewModel: ProfileViewModel? = navController
            .previousBackStackEntry?.let {
                hiltViewModel(it)
            }

        EditProfileView(
            navController = navController,
            profileViewModel = previousViewModel,
            popBackStack = { user ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(USER_KEY, user)
            }, mainViewModel = mainViewModel
        )
    }
}



