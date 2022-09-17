package com.example.ibrasaloonapp.presentation.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.presentation.components.CommonOtp
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.theme.Blue
import com.example.ibrasaloonapp.presentation.ui.Screen
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {

    val phone = viewModel.state.value.phone
    val phoneError = viewModel.state.value.phoneError
    val code = viewModel.state.value.verifyCode
    val progress = viewModel.uiState.value.progressBarState
    val uiMessage = viewModel.uiState.value.uiMessage
    val showCode = viewModel.state.value.showCode

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(LoginEvent.Reset)
    }

    DefaultScreenUI(
        progressBarState = if (showCode) progress else ProgressBarState.Idle,
        onRemoveUIComponent = { viewModel.onTriggerEvent(LoginEvent.OnRemoveHeadFromQueue) },
        uiComponent = uiMessage
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    onRest = { viewModel.onTriggerEvent(LoginEvent.Reset) }

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


        Bottom(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            progressBar = progress,
            buttonText = stringResource(
                id = R.string.login
            ),
            onClick = {
                onTriggerEvent(LoginEvent.SendAuthVerification)
            }
        )
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


@Composable
private fun Bottom(
    onClick: () -> Unit,
    progressBar: ProgressBarState = ProgressBarState.Idle,
    modifier: Modifier,
    buttonText: String
) {
    val loading = (progressBar is ProgressBarState.Loading)

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (progress, button) = createRefs()


        AnimatedVisibility(
            modifier = Modifier.constrainAs(progress) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            visible = loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }


        AnimatedVisibility(
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            visible = !loading, enter = fadeIn(), exit = fadeOut()
        ) {
            Button(
                onClick = onClick,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                ),
                modifier = modifier,
                contentPadding = PaddingValues(all = 16.dp),
            ) {
                Text(text = buttonText)
            }
        }


    }

}





