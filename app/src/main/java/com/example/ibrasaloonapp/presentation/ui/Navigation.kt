package com.example.ibrasaloonapp.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.*
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.DialogEvent
import com.example.ibrasaloonapp.domain.model.MenuItem
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.ui.appointment_list.AppointmentListView
import com.example.ibrasaloonapp.presentation.ui.book_appointment.BookAppointmentView
import com.example.ibrasaloonapp.presentation.ui.edit_profile.ChangePhoneNumberView
import com.example.ibrasaloonapp.presentation.ui.edit_profile.EditProfileView
import com.example.ibrasaloonapp.presentation.ui.edit_profile.EditProfileViewModel
import com.example.ibrasaloonapp.presentation.ui.home.*
import com.example.ibrasaloonapp.presentation.ui.login.LoginView
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileEvent
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileView
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileViewModel
import com.example.ibrasaloonapp.presentation.ui.signup.SignupEvent
import com.example.ibrasaloonapp.presentation.ui.signup.SignupView
import com.example.ibrasaloonapp.presentation.ui.signup.SignupViewModel
import com.example.ibrasaloonapp.presentation.ui.splash.SplashView
import com.example.ibrasaloonapp.presentation.ui.upload.IMAGE_KEY
import com.example.ibrasaloonapp.presentation.ui.upload.UploadImageView
import kotlinx.coroutines.launch

private const val TAG = "Navigation"

@Composable
fun Navigation(modifier: Modifier = Modifier, mainViewModel: MainActivityViewModel) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val user = mainViewModel.state.value.authData?.user
    val uiMessage = mainViewModel.uiState.value.uiMessage
    val networkStatus = mainViewModel.uiState.value.network
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    Log.d(TAG, "Navigation: ${currentDestination}")

    val events = mainViewModel.uiEvents

    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is MainUIEvent.Logout -> {
                        Log.d(TAG, "Navigation: Logged out")

                        scope.launch { scaffoldState.drawerState.close() }
                        val currentRout = navController.currentDestination?.route
                        currentRout?.let {
                            if (currentRout != Screen.Home.route && currentRout != Screen.Signup.route) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }

                    is MainUIEvent.LoggedIn -> {
                        Log.d(TAG, "Navigation: LoggedIn")
                        navController.navigate(Screen.Home.route){
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                            restoreState = true
                        }

                    }

                    is MainUIEvent.AuthDataReady -> {
                        Log.d(TAG, "Navigation: AuthDataReady")
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




    DefaultScreenUI(
        onRemoveUIComponent = { mainViewModel.onTriggerEvent(MainEvent.RemoveMessage) },
        uiComponent = uiMessage,
        dialogOnConfirm = {
            when (it) {
                is DialogEvent.Logout -> {
                    mainViewModel.onTriggerEvent(MainEvent.Logout)
                }
            }
        },
        networkStatus = networkStatus,
        onDismissNetworkMessage = { mainViewModel.onTriggerEvent(MainEvent.DismissNetworkMessage) },
        isSplashRoute = currentDestination == Screen.Splash.route
    ) {

        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            scaffoldState = scaffoldState,
            drawerBackgroundColor = Gray2,
            drawerGesturesEnabled = user != null && currentDestination != Screen.Splash.route,
            drawerContent =
            {
                DrawerHeader()
                DrawerBody(items = drawerItems, onClick = { item ->
                    if (item.id != "logout") {
                        Log.d(TAG, "Navigation: drawerItem ${item.id}")
                        navController.navigate(item.id) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }


                        scope.launch {
                            scaffoldState.drawerState.close()
                        }

                    } else {
                        mainViewModel.onTriggerEvent(MainEvent.ShowLogoutDialog)
                    }
                }, itemTextStyle = MaterialTheme.typography.body2)
            }
        ) {

            NavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
                splash(navController = navController, mainViewModel = mainViewModel)
                login(navController = navController)
                signup(navController = navController, mainViewModel = mainViewModel)
                home(navController = navController, mainViewModel = mainViewModel)
                appointmentList(navController = navController, mainViewModel = mainViewModel)
                bookAppointment(navController = navController, mainViewModel = mainViewModel)

                uploadImage(navController = navController, mainViewModel = mainViewModel)
                profile(navController = navController, mainViewModel = mainViewModel)
                editProfile(navController = navController, mainViewModel = mainViewModel)
                updatePhoneNumber(navController = navController, mainViewModel = mainViewModel)
            }
        }
    }
}

fun NavGraphBuilder.splash(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.Splash.route,
        arguments = emptyList()
    ) {
        SplashView(navController = navController, mainViewModel = mainViewModel)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.login(
    navController: NavController,
) {
    dialog(
        route = Screen.Login.route,
        arguments = emptyList(),
        dialogProperties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .fillMaxHeight(.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginView(navController = navController)
        }
    }
}


fun NavGraphBuilder.signup(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.Signup.route,
        arguments = emptyList()
    ) { backStackEntry ->
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
//
//        val appointment = backStackEntry
//            .savedStateHandle
//            .getLiveData<Appointment>(APPOINTMENT_KEY)
//            .observeAsState().value
//
//
//        appointment?.let {
//            viewModel.onTriggerEvent(HomeEvent.UpdateAppointment(appointment))
//        }
//        backStackEntry.savedStateHandle.remove<Appointment>(APPOINTMENT_KEY)

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
//                navController.previousBackStackEntry
//                    ?.savedStateHandle
//                    ?.set(APPOINTMENT_KEY, appointment)
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


        val updatedImagePath = backStackEntry
            .savedStateHandle
            .getLiveData<String>(IMAGE_KEY)
            .observeAsState().value

        val viewModel: ProfileViewModel = hiltViewModel()



        updatedImagePath?.let {
            viewModel.onTriggerEvent(ProfileEvent.UpdateImage(updatedImagePath))
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

        EditProfileView(
            navController = navController,
            mainViewModel = mainViewModel
        )
    }
}


fun NavGraphBuilder.updatePhoneNumber(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.UpdatePhoneNumber.route,
        arguments = Screen.UpdatePhoneNumber.arguments
    ) {
        val previousViewModel: EditProfileViewModel? = navController
            .previousBackStackEntry?.let {
                hiltViewModel(it)
            }

        previousViewModel?.let {
            ChangePhoneNumberView(
                navController = navController,
                editProfileViewModel = previousViewModel
            )
        }
    }
}


fun NavGraphBuilder.uploadImage(
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    composable(
        route = Screen.UploadImage.route,
        arguments = Screen.UploadImage.arguments
    ) {
        val previousViewModel: ProfileViewModel? = navController
            .previousBackStackEntry?.let {
                hiltViewModel(it)
            }


        UploadImageView(
            navController = navController,
            mainActivityViewModel = mainViewModel
        )
    }
}




