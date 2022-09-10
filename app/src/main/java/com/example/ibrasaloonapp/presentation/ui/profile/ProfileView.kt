package com.example.ibrasaloonapp.presentation.ui.profile

import android.util.Log
import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius.Companion.Zero
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen


private const val TAG = "ProfileView"

@Composable
fun ProfileView(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val user = viewModel.state.value.user
    val progress = viewModel.uiState.value.progressBarState

    user?.let {

        DefaultScreenUI(
            progressBarState = progress,
            onRemoveHeadFromQueue = { }) {

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
                        navController.navigate(Screen.EditProfile.route + "/${user.id}/${user.firstName}/${user.lastName}/${user.phone}")
                    }
                )


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 250.dp, max = 300.dp)
                        .offset(y = -16.dp)
                ) {

                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (user.image != null) "https://saloon-ibra-api.herokuapp.com/imgs/${user.image}" else null)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.person_place_holder),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.error_image_generic),
                        fallback = painterResource(id = R.drawable.person_place_holder),

                        )


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    .2f to Color.Black,
                                    1f to Black4,
                                ), alpha = .5f
                            )
                    )

                    Text(
                        modifier = Modifier
                            .align(BottomStart)
                            .padding(8.dp),
                        text = "${user.firstName} ${user.lastName}",
                        style = MaterialTheme.typography.h4,
                        color = White
                    )

                }


            }
        }

    }
}


@Composable
fun Top(modifier: Modifier = Modifier, navigateToUpdatePage: () -> Unit) {


    Surface(
        modifier = modifier,
        color = Orange,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        elevation = 8.dp
    ) {

        Column {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.Top)
                        .padding(8.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.profile),
                        color = Gray1,
                        style = MaterialTheme.typography.h2
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.align(CenterVertically),
                    style = MaterialTheme.typography.h4,
                    text = "Barber",
                    color = MaterialTheme.colors.onPrimary
                )


                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(1.dp, Gray1, CircleShape)
                        .align(Alignment.CenterVertically),
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