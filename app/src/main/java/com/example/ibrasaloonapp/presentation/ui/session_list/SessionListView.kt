package com.example.ibrasaloonapp.presentation.ui.session_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.SessionCard
import com.example.ibrasaloonapp.presentation.components.SessionListHeader
import com.example.ibrasaloonapp.presentation.ui.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionListView(
    navController: NavController,
    viewModel: SessionListViewModel = hiltViewModel(),
) {
    val queue = viewModel.state.value.errorQueue
    val sessions = viewModel.state.value.sessions
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
        onRemoveHeadFromQueue = { viewModel.onTriggerEvent(SessionListEvent.OnRemoveHeadFromQueue) }) {

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

                        SessionListHeader(navigateToAddAppointment = { navController.navigate(Screen.AddAppointment.route) })
                    }

                    itemsIndexed(
                        items = sessions,
                        key = { index, item -> item.id }) { index, item ->
                        SessionCard(session = item, modifier = Modifier.animateItemPlacement())
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