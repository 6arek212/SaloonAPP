package com.example.ibrasaloonapp.presentation.ui.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.ui.Screen


private const val TAG = "ProfileView"

@Composable
fun ProfileView(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    Log.d(TAG, "ProfileView: ${viewModel.toString()}")

    val user = viewModel.state.value.user
    val progress = viewModel.uiState.value.progressBarState

    user?.let {

        DefaultScreenUI(
            progressBarState = progress,
            onRemoveHeadFromQueue = { }) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(8.dp),
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.profile),
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h2
                    )

                    IconButton(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        onClick = {
                            navController.navigate(Screen.EditProfile.route + "/${user.id}/${user.firstName}/${user.lastName}/${user.phone}")
                        }) {
                        Icon(imageVector = Icons.Filled.BorderColor, contentDescription = "")
                    }
                }


                Divider(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Gray2)
                )

                Spacer(modifier = Modifier.padding(24.dp))


                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = 8.dp,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {


                        Image(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .align(CenterHorizontally)
                                .border(2.dp, Gray2, CircleShape),
                            painter = painterResource(id = R.drawable.woker1),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )


                        Spacer(modifier = Modifier.padding(24.dp))


                        Text(
                            text = "${stringResource(id = R.string.role)}: ${user.role}",
                            style = MaterialTheme.typography.h4
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(
                            text = "${stringResource(id = R.string.first_name)}: ${user.firstName}",
                            style = MaterialTheme.typography.h4
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(
                            text = "${stringResource(id = R.string.last_name)}: ${user.lastName}",
                            style = MaterialTheme.typography.h4
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(text = "${stringResource(id = R.string.phone)}: ${user.phone}", style = MaterialTheme.typography.h4)
                    }
                }

            }
        }

    }
}