package com.example.ibrasaloonapp.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.VerticalImageChip
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen


private const val TAG = "ProfileView"

@Composable
fun ProfileView(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel
) {
    val user = mainViewModel.state.value.authData?.user
    val progress = viewModel.uiState.collectAsState().value.progressBarState
    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage


    user?.let {

        DefaultScreenUI(
            progressBarState = progress,
            onRemoveUIComponent = { },
            uiComponent = uiMessage
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Gray2),
            ) {


                Top(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f),
                    navigateToUpdatePage = {
                        navController.navigate(Screen.EditProfile.route + "/${user.id}/${user.firstName}/${user.lastName}/${user.phone}") {
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