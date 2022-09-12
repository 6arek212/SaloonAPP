package com.example.ibrasaloonapp.presentation.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.TimePatterns
import com.example.ibrasaloonapp.core.stringDateFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.book_appointment.BookAppointmentEvent
import com.example.ibrasaloonapp.presentation.ui.login.LoginView
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navController: NavController,
    mainViewModel: MainActivityViewModel,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val appointment = viewModel.state.value.appointment
    val workers = viewModel.state.value.workers
    val refreshing = viewModel.state.value.refreshing
    val user = mainViewModel.state.value.authData?.user
    val showLoginDialog = viewModel.state.value.showLoginDialog

    DefaultScreenUI(onRemoveHeadFromQueue = { viewModel.onTriggerEvent(HomeEvent.OnRemoveHeadFromQueue) }) {
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
                    isRefreshing = refreshing,
                    onTriggerEvent = viewModel::onTriggerEvent,
                    navController = navController,
                    navigateToBookAppointment = { navController.navigate(Screen.BookAppointment.route) },
                    showLoginDialog = showLoginDialog,
                    onDismissLoginDialog = { viewModel.onTriggerEvent(HomeEvent.DismissLoginDialog) },
                    onShowLoginDialog = { viewModel.onTriggerEvent(HomeEvent.ShowLoginDialog) },
                    user = user,
                    onLogin = { authData ->
                        mainViewModel.onTriggerEvent(
                            MainEvent.Login(
                                authData
                            )
                        )
                        viewModel.onTriggerEvent(HomeEvent.Refresh)
                    }
                )
            },
            frontLayerElevation = 10.dp,
            frontLayerBackgroundColor = Gray1
        )
    }

}


@Composable
fun BackLayer(user: User?) {

    user?.let {
        Column {
            ImageName(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp),
                firstName = it.firstName,
                lastName = it.lastName
            )
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }

}

@Composable
fun FrontLayer(
    appointment: Appointment?,
    workers: List<User>,
    isRefreshing: Boolean,
    onTriggerEvent: (HomeEvent) -> Unit,
    navigateToBookAppointment: () -> Unit,
    navController: NavController,
    showLoginDialog: Boolean,
    onDismissLoginDialog: () -> Unit,
    onShowLoginDialog: () -> Unit,
    user: User?,
    onLogin: (AuthData) -> Unit,
) {
    val scrollState = rememberScrollState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { onTriggerEvent(HomeEvent.Refresh) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {


            Header(
                appointment = appointment,
                navigateToBookAppointment = navigateToBookAppointment,
                navController = navController,
                showLoginDialog = showLoginDialog,
                onDismissLoginDialog = onDismissLoginDialog,
                onShowLoginDialog = onShowLoginDialog,
                user = user,
                onLogin = onLogin
            )



            OurStaff(workers = workers)

            Spacer(modifier = Modifier.padding(16.dp))

            Stories()

            Spacer(modifier = Modifier.padding(16.dp))

            AboutUs()
        }
    }
}


@Composable
fun Header(
    modifier: Modifier = Modifier,
    appointment: Appointment?,
    navigateToBookAppointment: () -> Unit,
    navController: NavController,
    showLoginDialog: Boolean,
    onDismissLoginDialog: () -> Unit,
    onShowLoginDialog: () -> Unit,
    user: User?,
    onLogin: (AuthData) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp)
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


        if (user != null) {
            Appointment(
                appointment = appointment,
                navigateToBookAppointment = navigateToBookAppointment
            )
        } else {
            NotLoggedIn(
                navController = navController,
                showLoginDialog = showLoginDialog,
                onDismissLoginDialog = onDismissLoginDialog,
                onShowLoginDialog = onShowLoginDialog,
                onLogin = onLogin
            )
        }


    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotLoggedIn(
    onShowLoginDialog: () -> Unit,
    onDismissLoginDialog: () -> Unit,
    onLogin: (AuthData) -> Unit,
    showLoginDialog: Boolean,
    navController: NavController
) {

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(1.dp, Gray1),
            shape = MaterialTheme.shapes.large,
            onClick = { onShowLoginDialog() }
        ) {
            Text(
                text = stringResource(id = R.string.login_or_create_account),
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


    if (showLoginDialog)
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = onDismissLoginDialog,
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .fillMaxHeight(.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoginView(
                    navController = navController,
                    onLoggedIn = { authData ->
                        onDismissLoginDialog()
                        onLogin(authData)
                    })
            }
        }
}


@Composable
fun Appointment(
    modifier: Modifier = Modifier,
    appointment: Appointment?,
    navigateToBookAppointment: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(1.dp, Gray1),
            shape = MaterialTheme.shapes.large,
            onClick = { navigateToBookAppointment() }
        ) {
            Text(
                text = stringResource(id = R.string.book),
                style = MaterialTheme.typography.h4,
                color = Gray1,
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))




        appointment?.let {

            Text(
                text = stringResource(id = R.string.you_have_appointment),
                color = Color.White,
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "${
                    stringDateFormat(
                        appointment.startTime,
                        TimePatterns.EEEE_MM_DD,
                        LocalContext.current
                    )
                } " +
                        "${stringResource(id = R.string.at)} " +
                        stringDateFormat(
                            appointment.startTime,
                            TimePatterns.TIME_ONLY,
                            LocalContext.current
                        ),
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

        if (appointment == null) {

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
            items(items = workers) { worker ->
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            StoryCard(image = R.drawable.story1)
            StoryCard(image = R.drawable.story2)
            StoryCard(image = R.drawable.story3)
            StoryCard(image = R.drawable.story4)
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
//    HomeView()
}