package com.example.ibrasaloonapp.presentation.ui.appointment_list

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.samples.crane.base.CraneScreen
import androidx.compose.samples.crane.base.CraneTabBar
import androidx.compose.samples.crane.base.CraneTabs
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen


@Composable
fun AppointmentListView(
    navController: NavController,
    viewModel: AppointmentsListViewModel = hiltViewModel(),
    authViewModel: MainActivityViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(AppointmentListEvent.GetAppointments)
    }

    val authData = authViewModel.state.value
    val queue = viewModel.state.value.errorQueue
    val activeAppointments = viewModel.state.value.activeAppointments
    val historyAppointments = viewModel.state.value.historyAppointments
    val progressBar = viewModel.state.value.progressBarState
//    val listState = rememberLazyGridState()
//    val showButton = remember {
//        derivedStateOf {
//            listState.firstVisibleItemIndex > 0
//        }
//    }


    DefaultScreenUI(
        onRemoveHeadFromQueue = { viewModel.onTriggerEvent(AppointmentListEvent.OnRemoveHeadFromQueue) },
        queue = queue,
        progressBarState = progressBar,
        dialogOnConfirm = {}
    ) {
        AppointmentListContent(
            authData = authData.authData,
            navigateToBookAppointment = { navController.navigate(Screen.BookAppointment.route) },
            activeAppointments = activeAppointments,
            historyAppointments = historyAppointments,
            cancelAppointment = { id, index ->
                viewModel.onTriggerEvent(
                    AppointmentListEvent.CancelAppointment(
                        id = id,
                        index = index
                    )
                )
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppointmentListContent(
    modifier: Modifier = Modifier,
    activeAppointments: List<Appointment>,
    historyAppointments: List<Appointment>,
    authData: AuthData?,
    navigateToBookAppointment: () -> Unit,
    cancelAppointment: (id: String, index: Int) -> Unit
) {

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerScrimColor = Color.Unspecified,

        appBar = {
//            HomeTabBar(CraneScreen.Home, onTabSelected = { })
        },
        backLayerContent = {
            Header(
                authData = authData,
                navigateToBookAppointment = navigateToBookAppointment,
                activeAppointments = activeAppointments,
                cancelAppointment = cancelAppointment
            )
        },
        frontLayerContent = {
            HistorySection(historyAppointments = historyAppointments)
        }
    )

}

@Composable
private fun HomeTabBar(
    tabSelected: CraneScreen,
    onTabSelected: (CraneScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    CraneTabBar(
        modifier = modifier
            .wrapContentWidth()
            .sizeIn(maxWidth = 500.dp),
        onMenuClicked = {}
    ) { tabBarModifier ->
        CraneTabs(
            modifier = tabBarModifier,
            titles = CraneScreen.values().map { it.name },
            tabSelected = tabSelected,
            onTabSelected = { newTab -> onTabSelected(CraneScreen.values()[newTab.ordinal]) }
        )
    }
}


@Composable
fun Header(
    modifier: Modifier = Modifier,
    authData: AuthData?,
    navigateToBookAppointment: () -> Unit,
    activeAppointments: List<Appointment>,
    cancelAppointment: (id: String, index: Int) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        ImageAndName(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 24.dp),
            firstName = authData?.user?.firstName ?: "T",
            lastName = authData?.user?.lastName ?: "HZ"
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Orange),
            shape = MaterialTheme.shapes.medium,
            onClick = { navigateToBookAppointment() }) {
            Text(
                text = "Book Appointment",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onPrimary,
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))


        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = activeAppointments) { index, item ->
                ActiveAppointmentCard(
                    modifier = Modifier
                        .fillParentMaxWidth(.95f),
                    appointment = item,
                    onCancel = {
                        cancelAppointment(item.id, index)
                    }
                )
            }
        }

    }


}


@Composable
fun HistorySection(
    modifier: Modifier = Modifier,
    historyAppointments: List<Appointment>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(3.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Gray2)
                .align(Alignment.CenterHorizontally)
        )


        Text(
            text = "History",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground
        )

        Divider(
            modifier = Modifier
                .padding(top = 2.dp)
                .fillMaxWidth()
                .height(1.dp), color = Gray2
        )

        Spacer(modifier = Modifier.padding(8.dp))


        LazyVerticalGrid(
            modifier = modifier
                .fillMaxWidth(),
            columns = GridCells.Adaptive(120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {


            items(items = historyAppointments) { item ->
                AppointmentCard(date = item.date ?: "", time = item.time ?: "")
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (historyAppointments.isEmpty()) {
                    Empty(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = "No appointments to show"
                    )
                }
            }
        }
    }


}








