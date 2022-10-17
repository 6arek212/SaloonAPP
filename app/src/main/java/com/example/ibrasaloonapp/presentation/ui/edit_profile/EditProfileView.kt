package com.example.ibrasaloonapp.presentation.ui.edit_profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.components.BackButton
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch


private const val TAG = "EditProfileView"


@Composable
fun EditProfileView(
    navController: NavController,
    mainViewModel: MainActivityViewModel,
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    val firstName = viewModel.state.value.firstName
    val lastName = viewModel.state.value.lastName
    val firstNameError = viewModel.state.value.firstNameError
    val lastNameError = viewModel.state.value.lastNameError
    val phone = mainViewModel.state.value.authData?.user?.phone
    val progress = viewModel.uiState.collectAsState().value.progressBarState
    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage


    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    DefaultScreenUI(
        uiComponent = uiMessage,
        progressBarState = progress,
        onRemoveUIComponent = { viewModel.onTriggerEvent(EditProfileEvent.OnRemoveHeadFromQueue) }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { focusManager.clearFocus() }
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                BackButton(onClick = { navController.popBackStack() }, tint = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = stringResource(id = R.string.edit_profile),
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h2
                )
            }




            Divider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Gray2)
            )

            Spacer(modifier = Modifier.padding(24.dp))


            OutlinedTextField(
                modifier = Modifier,
                label = {
                    Text(text = stringResource(id = R.string.first_name))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { s ->
                    viewModel.onTriggerEvent(EditProfileEvent.OnFirstNameChanged(s))
                },
                value = firstName,
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Person, stringResource(id = R.string.first_name))
                },
                textStyle = MaterialTheme.typography.h4
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = firstNameError ?: "",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2
            )


            Spacer(modifier = Modifier.padding(8.dp))


            OutlinedTextField(
                modifier = Modifier,
                label = {
                    Text(text = stringResource(id = R.string.last_name))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { s ->
                    viewModel.onTriggerEvent(EditProfileEvent.OnLastNameChanged(s))
                },
                value = lastName,
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Info, stringResource(id = R.string.last_name))
                },
                textStyle = MaterialTheme.typography.h4
            )

            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = lastNameError ?: "",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2
            )


            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedTextField(
                modifier = Modifier.clickable {
                    navController.navigate(Screen.UpdatePhoneNumber.route) {
                        popUpTo(Screen.EditProfile.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = stringResource(id = R.string.phone))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { s ->
                    viewModel.onTriggerEvent(EditProfileEvent.OnPhoneChanged(s))
                },
                value = phone ?: "",
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Phone, stringResource(id = R.string.phone))
                },
                textStyle = MaterialTheme.typography.h4,
                readOnly = true,
                enabled = false
            )


            Spacer(modifier = Modifier.padding(24.dp))

            Button(
                contentPadding = PaddingValues(16.dp),
                onClick = {
                    viewModel.onTriggerEvent(EditProfileEvent.UpdateProfile)
                }) {
                Text(text = stringResource(id = R.string.update))
            }


        }

    }


}