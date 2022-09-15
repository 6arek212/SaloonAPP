package com.example.ibrasaloonapp.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.VerticalImageChip
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen
import kotlinx.coroutines.launch


private const val TAG = "ProfileView"

@Composable
fun ProfileView(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel
) {
    val user = viewModel.state.value.user
    val progress = viewModel.uiState.value.progressBarState
    val events = viewModel.uiEvents
    val uiMessage = viewModel.uiState.value.uiMessage

    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is MainUIEvent.Logout -> {
                        mainViewModel.onTriggerEvent(MainEvent.Logout)
                    }
                }
            }
        }
    }
    user?.let {

        DefaultScreenUI(
            progressBarState = progress,
            onRemoveUIComponent = { },
            uiComponent = uiMessage
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            ) {


                Top(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f),
                    navigateToUpdatePage = {
                        navController.navigate(Screen.EditProfile.route + "/${user.id}/${user.firstName}/${user.lastName}/${user.phone}"){
                            popUpTo(Screen.Profile.route)
                            launchSingleTop = true
                        }
                    }
                )

                VerticalImageChip(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .offset(y = -80.dp)
                        .zIndex(1f),
                    text = "${user.firstName} ${user.lastName}",
                    onClick = {
                        navController.navigate(Screen.UploadImage.route) {
                            popUpTo(Screen.Profile.route)
                            launchSingleTop = true
                        }
                    },
                    isSelected = false,
                    url = user.image,
                    imageSize = 200.dp,
                )


                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.h4,
                    color = White
                )


            }
        }

    }
}


@Composable
fun Top(modifier: Modifier = Modifier, navigateToUpdatePage: () -> Unit) {


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        color = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.profile),
                    color = Gray1,
                    style = MaterialTheme.typography.h2
                )


                IconButton(
                    modifier = Modifier.clip(CircleShape),
                    onClick = {
                        navigateToUpdatePage()
                    }) {
                    Icon(
                        tint = MaterialTheme.colors.onPrimary,
                        imageVector = Icons.Filled.BorderColor,
                        contentDescription = "",
                    )
                }
            }
        }
    }


}