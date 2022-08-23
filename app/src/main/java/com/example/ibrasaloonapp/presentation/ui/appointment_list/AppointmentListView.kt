package com.example.ibrasaloonapp.presentation.ui.appointment_list

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.SessionCard
import com.example.ibrasaloonapp.presentation.components.SessionListHeader
import com.example.ibrasaloonapp.presentation.ui.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppointmentListView(
    navController: NavController,
    viewModel: SessionListViewModel = hiltViewModel(),
    authViewModel: MainActivityViewModel = hiltViewModel()
) {
    val authData = authViewModel.state.value
    val queue = viewModel.state.value.errorQueue
    val appointments = viewModel.state.value.appointments
    val progressBar = viewModel.state.value.progressBarState
    val listState = rememberLazyGridState()
    val showButton = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    val coroutineScope = rememberCoroutineScope()


    DefaultScreenUI(
        queue = queue,
        progressBarState = progressBar,
        onRemoveHeadFromQueue = { viewModel.onTriggerEvent(AppointmentListEvent.OnRemoveHeadFromQueue) }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {


                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    contentPadding = PaddingValues(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {

                        SessionListHeader(navigateToAddAppointment = { navController.navigate(Screen.BookAppointment.route) })
                    }

                    itemsIndexed(
                        items = appointments,
                        key = { index, item -> item.id }) { index, item ->
                        SessionCard(appointment = item, modifier = Modifier.animateItemPlacement())
                    }
                }
            }



            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                visible = showButton.value
            ) {
                Button(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(),
                    border = BorderStroke(2.dp, Color.Red),
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = "")
                }
            }
        }

    }

}