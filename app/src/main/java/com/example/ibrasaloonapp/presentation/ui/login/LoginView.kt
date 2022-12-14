package com.example.ibrasaloonapp.presentation.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.CommonOtp
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.ProgressButton
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.theme.Blue
import com.example.ibrasaloonapp.presentation.ui.Screen

@Composable
fun LoginView(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel
) {

    val phone = viewModel.state.value.phone
    val phoneError = viewModel.state.value.phoneError
    val code = viewModel.state.value.verifyCode
    val progress = viewModel.uiState.collectAsState().value.progressBarState
    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage
    val showCode = viewModel.state.value.showCode
    val isLoggedIn = viewModel.state.value.isLoggedIn
    val workerMode = mainViewModel.state.value.workerMode

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(key1 = viewModel.state.value.isLoggedIn) {
        if (isLoggedIn) {
            if (workerMode) {
                navController.navigate(Screen.WorkerAppointmentsList.route) {
                    popUpTo(Screen.Home.route)
                    launchSingleTop = true
                    restoreState = true
                }
            } else {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route)
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }


    DefaultScreenUI(
        progressBarState = if (showCode) progress else ProgressBarState.Idle,
        onRemoveUIComponent = { viewModel.onTriggerEvent(LoginEvent.OnRemoveHeadFromQueue) },
        uiComponent = uiMessage
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
                .verticalScroll(scrollState)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { focusManager.clearFocus() }
        ) {

            SubTitle(text = stringResource(id = R.string.login))


            Spacer(modifier = Modifier.padding(12.dp))

            Text(
                text = stringResource(id = R.string.lets_start),
                style = MaterialTheme.typography.h2
            )
            Text(
                text = stringResource(id = R.string.the_login_process),
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.padding(2.dp))

            AnimatedVisibility(visible = showCode) {
                CommonOtp(
                    onChangeFocus = { place, str ->
                        viewModel.onTriggerEvent(
                            LoginEvent.OnCodeDigitChanged(
                                place,
                                str
                            )
                        )
                    },
                    code = code,
                    moveFocus = focusManager::moveFocus,
                    onRest = { viewModel.onTriggerEvent(LoginEvent.Reset) },
                    sendAgain = { viewModel.onTriggerEvent(LoginEvent.SendAuthVerification(true)) },
                    isLoading = progress == ProgressBarState.Loading
                )
            }


            AnimatedVisibility(visible = !showCode) {
                PhoneSection(
                    phone = phone,
                    phoneError = phoneError,
                    progress = progress,
                    onTriggerEvent = viewModel::onTriggerEvent
                )
            }

            if (!showCode) {
                Spacer(modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(id = R.string.dont_have_an_account),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        modifier = Modifier.clickable { navController.navigate(Screen.Signup.route) },
                        text = stringResource(id = R.string.create_account),
                        style = MaterialTheme.typography.caption,
                        color = Blue
                    )

                }
            }

        }

    }
}


@Composable
private fun PhoneSection(
    phone: String,
    phoneError: String?,
    progress: ProgressBarState,
    onTriggerEvent: (LoginEvent) -> Unit
) {


    Column(
        modifier = Modifier,
        horizontalAlignment = CenterHorizontally,
    ) {


        TextFields(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            phone = phone,
            onTriggerEvent = onTriggerEvent,
            phoneError = phoneError,
        )


        Spacer(modifier = Modifier.padding(vertical = 16.dp))


        ProgressButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { onTriggerEvent(LoginEvent.SendAuthVerification(false)) },
            color = MaterialTheme.colors.primary,
            progressColor = MaterialTheme.colors.background,
            loading = progress == ProgressBarState.Loading
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}


@Composable
private fun TextFields(
    modifier: Modifier,
    phone: String,
    phoneError: String?,
    onTriggerEvent: (LoginEvent) -> Unit,
) {

    Column {

        OutlinedTextField(
            modifier = modifier,
            label = {
                Text(text = stringResource(id = R.string.phone))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = { s ->
                onTriggerEvent(LoginEvent.OnPhoneValueChange(s))
            },
            value = phone,
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Phone, "phone")
            },
            textStyle = MaterialTheme.typography.h4
        )

        phoneError?.let {
            Text(
                modifier = modifier,
                text = phoneError,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2
            )
        }
    }
}






