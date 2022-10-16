package com.example.ibrasaloonapp.presentation.ui.users_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.components.UserCard
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.StatsCard
import com.example.ibrasaloonapp.presentation.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun UsersView(
    viewModel: UsersViewModel = hiltViewModel(),
    navigateToUserDetails: (User) -> Unit
) {
    val users = viewModel.state.value.users
    val usersCount = viewModel.state.value.usersCount
    val newUsersCount = viewModel.state.value.newUsersCount
    val search = viewModel.state.value.search
    val refresh = viewModel.state.value.refresh
    val progress = viewModel.uiState.collectAsState().value.progressBarState
    val swipeState = rememberSwipeRefreshState(isRefreshing = refresh)

    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val listMoved by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    val shadowTop = animateDpAsState(
        targetValue = if (listMoved) {
            8.dp
        } else {
            0.dp
        }, animationSpec = tween(
            durationMillis = 500
        )
    )



    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(UsersListEvent.GetUsersList)
    }

    DefaultScreenUI(progressBarState = progress, onRemoveUIComponent = { /*TODO*/ }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray1)
        ) {

            Surface(
                elevation = shadowTop.value, shape = RoundedCornerShape(
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                bottomEnd = 16.dp,
                                bottomStart = 16.dp
                            )
                        )
                        .background(MaterialTheme.colors.primary)
                        .padding(8.dp),
                ) {
                    SubTitle(
                        text = stringResource(id = R.string.customers),
                        textColor = MaterialTheme.colors.onPrimary
                    )

                    Spacer(modifier = Modifier.padding(16.dp))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        StatsCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(id = R.string.users),
                            value = usersCount?.let { "$it" } ?: "",
                            visible = true,
                            leadingIcon = ImageVector.vectorResource(id = R.drawable.graph),
                            cardColor = Orange
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        StatsCard(
                            title = stringResource(R.string.new_users),
                            value = newUsersCount?.let { "$it" } ?: "",
                            visible = true,
                            leadingIcon = ImageVector.vectorResource(id = R.drawable.diagram),
                            cardColor = GrayBlue
                        )
                    }



                    Spacer(modifier = Modifier.padding(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    MaterialTheme.colors.surface,
                                    MaterialTheme.shapes.large,
                                )
                                .padding(8.dp),
                            value = search,
                            onValueChange = { s ->
                                viewModel.onTriggerEvent(
                                    UsersListEvent.OnSearchChanged(s)
                                )
                            },
                            textStyle = MaterialTheme.typography.body2,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                viewModel.onTriggerEvent(
                                    UsersListEvent.GetUsersList
                                )
                                focusManager.clearFocus()
                            }),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Icon(
                                        imageVector = Icons.Outlined.Search,
                                        contentDescription = stringResource(id = R.string.search)
                                    )
                                    if (search.isEmpty())
                                        Text(
                                            text = "${stringResource(id = R.string.search)}...",
                                            color = Black1.copy(alpha = .3f),
                                            style = MaterialTheme.typography.body2
                                        )
                                    innerTextField()
                                }
                            },

                            )

                        Spacer(modifier = Modifier.padding(2.dp))


                        Button(modifier = Modifier,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            border = BorderStroke(1.dp, Gray1),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Orange),
                            onClick = {
                                viewModel.onTriggerEvent(
                                    UsersListEvent.GetUsersList
                                )
                                focusManager.clearFocus()
                            }) {
                            Text(
                                text = stringResource(id = R.string.search),
                                color = MaterialTheme.colors.onPrimary,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }

            SwipeRefresh(
                state = swipeState,
                onRefresh = { viewModel.onTriggerEvent(UsersListEvent.Refresh) }) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = listState
                ) {

                    itemsIndexed(items = users) { index, item ->
                        UserCard(user = item, onClick = { navigateToUserDetails(item) })
                    }
                }
            }
        }
    }


}