package com.example.ibrasaloonapp.presentation.ui.edit_profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch


private const val TAG = "EditProfileView"



@Composable
fun EditProfileView(
    navController: NavController,
    profileViewModel: ProfileViewModel?,
    mainViewModel: MainActivityViewModel,
    viewModel: EditProfileViewModel = hiltViewModel()
) {


    profileViewModel?.let {
        Log.d(TAG, "EditProfileView: ${profileViewModel}")
        val firstName = viewModel.state.value.firstName
        val lastName = viewModel.state.value.lastName
        val phone = viewModel.state.value.phone
        val progress = viewModel.uiState.value.progressBarState
        val uiMessage = viewModel.uiState.value.uiMessage



        DefaultScreenUI(
            uiComponent = uiMessage,
            progressBarState = progress,
            onRemoveUIComponent = { viewModel.onTriggerEvent(EditProfileEvent.OnRemoveHeadFromQueue) }) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(16.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back arrow"
                        )
                    }

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
                        Icon(Icons.Filled.Person, "first name")
                    },
                    textStyle = MaterialTheme.typography.h4
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
                        Icon(Icons.Filled.Info, "last name")
                    },
                    textStyle = MaterialTheme.typography.h4
                )

                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    modifier = Modifier,
                    label = {
                        Text(text = "Phone")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { s ->
                        viewModel.onTriggerEvent(EditProfileEvent.OnPhoneChanged(s))
                    },
                    value = phone,
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Filled.Phone, stringResource(id = R.string.phone))
                    },
                    textStyle = MaterialTheme.typography.h4
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


}