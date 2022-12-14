package com.example.ibrasaloonapp.presentation.ui.home

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.TimePatterns
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.navigateToWaze
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

private const val TAG = "HomeView"

@Composable
fun HomeView(
    navController: NavController,
    mainViewModel: MainActivityViewModel,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val isLoggedIn = mainViewModel.state.value.isLoggedIn
    val appointment = viewModel.state.value.appointment
    val workers = viewModel.state.value.workers
    val refreshing = viewModel.state.value.refreshing
    val user = mainViewModel.state.value.authData?.user
    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage
    val progress = viewModel.uiState.collectAsState().value.progressBarState

    LaunchedEffect(key1 = isLoggedIn) {
        viewModel.onTriggerEvent(HomeEvent.GetData(isAuthed = isLoggedIn))
    }



    DefaultScreenUI(
        uiComponent = uiMessage,
        progressBarState = progress,
        onRemoveUIComponent = { viewModel.onTriggerEvent(HomeEvent.OnRemoveHeadFromQueue) }) {
        Home(
            progress = progress == ProgressBarState.Loading,
            onRefresh = { viewModel.onTriggerEvent(HomeEvent.Refresh(isAuthed = user != null)) },
            onNavigateToLogin = {
                                navController.navigate(Screen.Login.route)
            },
            navigateToBookAppointment = {
                navController.navigate(Screen.BookAppointment.route) {
                    popUpTo(Screen.Home.route) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            },

            user = user,
            appointment = appointment,
            workers = workers,
            refreshing = refreshing,
        )
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    user: User?,
    appointment: Appointment?,
    workers: List<User>,
    refreshing: Boolean,
    progress: Boolean,
    onRefresh: () -> Unit,
    navigateToBookAppointment: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {

    SwipeRefresh(
        state = rememberSwipeRefreshState(refreshing),
        onRefresh = { onRefresh() }) {
        BackdropScaffold(
            scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
            frontLayerScrimColor = Color.Unspecified,
            appBar = {
                HomeAppBar()
            },
            backLayerContent = {
                BackLayer(user)
            },
            frontLayerContent = {
                FrontLayer(
                    appointment = appointment,
                    workers = workers,
                    navigateToBookAppointment = navigateToBookAppointment,
                    onShowLoginDialog = onNavigateToLogin,
                    user = user,
                    isLoading = progress
                )
            },
            frontLayerElevation = 10.dp,
            frontLayerBackgroundColor = Gray1
        )
    }
}


@Composable
fun BackLayer(user: User?) {

    if (user != null && user.firstName.isNotEmpty() && user.lastName.isNotEmpty()) {
        Column {
            ImageName(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp),
                firstName = user.firstName,
                lastName = user.lastName
            )
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }


}

@Composable
fun FrontLayer(
    appointment: Appointment?,
    workers: List<User>,
    navigateToBookAppointment: () -> Unit,
    onShowLoginDialog: () -> Unit,
    user: User?,

    isLoading: Boolean

) {
    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {


        Header(
            appointment = appointment,
            navigateToBookAppointment = navigateToBookAppointment,
            onShowLoginDialog = onShowLoginDialog,
            user = user,
            isLoading = isLoading
        )

        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {

            AnimatedVisibility(visible = workers.isNotEmpty()) {
                Column() {
                    OurStaff(workers = workers)
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }


            Stories()

            Spacer(modifier = Modifier.padding(8.dp))

            AboutUs()
        }

    }
}


@Composable
fun Header(
    modifier: Modifier = Modifier,
    appointment: Appointment?,
    navigateToBookAppointment: () -> Unit,
    onShowLoginDialog: () -> Unit,
    user: User?,

    isLoading: Boolean
) {

    Log.d(TAG, "Header: $appointment  $user   $isLoading")



    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 500.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Orange,
                        Orange2
                    )
                )
            )
            .padding(8.dp, 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        AnimatedVisibility(visible = user != null) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedButton(
                    modifier = Modifier,
                    contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
                    border = BorderStroke(1.dp, Gray1),
                    shape = MaterialTheme.shapes.large,
                    onClick = { navigateToBookAppointment() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(
                        text = stringResource(id = R.string.book),
                        style = MaterialTheme.typography.h4,
                        color = Gray1,
                    )
                }

                Spacer(modifier = Modifier.padding(16.dp))
            }
        }




        if (user != null) {

            AnimatedVisibility(visible = appointment != null && !isLoading) {
                Appointment(
                    appointment = appointment,
                    navigateToBookAppointment = navigateToBookAppointment
                )
            }

            AnimatedVisibility(visible = appointment == null && !isLoading) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.you_dont_have_appointment),
                        color = Color.White,
                        style = MaterialTheme.typography.h3,
                        lineHeight = 30.sp
                    )
                }

            }
        }

        AnimatedVisibility(visible = user == null && !isLoading) {
            NotLoggedIn(onShowLoginDialog = onShowLoginDialog)
        }
    }

}


@Composable
fun NotLoggedIn(
    onShowLoginDialog: () -> Unit
) {

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedButton(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
            border = BorderStroke(1.dp, Gray1),
            shape = MaterialTheme.shapes.large,
            onClick = onShowLoginDialog,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
        ) {

            //stringResource(id = R.string.login_or_create_account)
            Text(
                text = stringResource(R.string.login_or_signup),
                style = MaterialTheme.typography.h4,
                color = Gray1,
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))


        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.hello_there_lets_login_first),
            color = Color.White,
            style = MaterialTheme.typography.h3,
            lineHeight = 30.sp
        )

    }
}


@Composable
fun Appointment(
    modifier: Modifier = Modifier,
    appointment: Appointment?,
    navigateToBookAppointment: () -> Unit
) {
    val context = LocalContext.current
    var date by rememberSaveable {
        mutableStateOf(appointment?.startTime)
    }


    LaunchedEffect(Unit) {
        if (appointment == null)
            return@LaunchedEffect

        date = "${
            stringDateFormat(
                appointment.startTime,
                TimePatterns.EEEE_MM_DD,
                context
            )
        } " +
                "${context.getString(R.string.at)} " +
                stringDateFormat(
                    appointment.startTime,
                    TimePatterns.TIME_ONLY,
                    context
                )
    }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (appointment != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${stringResource(id = R.string.you_have_appointment)} ${
                        stringResource(
                            id = R.string.for_a
                        )
                    } ${appointment.service?.title}",
                    color = Color.White,
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = date ?: "",
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.padding(4.dp))


                ImageChip(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${appointment.worker.firstName} ${appointment.worker.lastName}",
                    onClick = { },
                    isSelected = false,
                    url = appointment.worker.image
                )
            }
        }


        Spacer(modifier = Modifier.padding(16.dp))

        TextButton(
            modifier = Modifier,
            onClick = { navigateToWaze(context) }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(25.dp),
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = stringResource(R.string.find_us),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.background
                )
            }
        }

    }
}


@Composable
fun OurStaff(workers: List<User>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(White)
    ) {
        SubTitle(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.our_staff)
        )

        Spacer(modifier = Modifier.padding(8.dp))



        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterHorizontally),
        ) {
            items(items = workers, key = { user -> user.id }) { worker ->
                AnimationBox(
                    enter = expandHorizontally() + fadeIn(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    VerticalImageChip(
                        modifier = Modifier,
                        text = "${worker.firstName} ${worker.lastName}",
                        onClick = {},
                        isSelected = false,
                        url = worker.image,
                        imageSize = 100.dp
                    )
                }
            }
        }


    }


}


@Composable
fun Stories() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(White)
    ) {
        SubTitle(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.stories)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            item {
                StoryCard(image = R.drawable.story1)
            }

            item {
                StoryCard(image = R.drawable.story2)
            }

            item {
                StoryCard(image = R.drawable.story3)
            }

            item {
                StoryCard(image = R.drawable.story4)
            }
        }

    }
}


@Composable
fun AboutUs() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(White)
            .padding(bottom = 20.dp)
    ) {
        SubTitle(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.about_us)
        )


        Spacer(modifier = Modifier.padding(8.dp))


        Box(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.about_us_details),
                style = MaterialTheme.typography.body2,
                color = Black4
            )
        }
    }
}


@Preview(showBackground = false)
@Composable
fun HomePreview() {
    AppTheme() {

        Home(
            user = User("", "tarik", "husin", "", ""),
            appointment = null,
            workers = listOf(),
            refreshing = false,
            progress = false,
            onRefresh = { /*TODO*/ },
            navigateToBookAppointment = { /*TODO*/ },
            onNavigateToLogin = { /*TODO*/ },
        )
    }

}