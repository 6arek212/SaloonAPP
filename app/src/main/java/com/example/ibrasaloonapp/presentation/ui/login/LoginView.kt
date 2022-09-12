package com.example.ibrasaloonapp.presentation.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.components.TimeCircularProgressBar
import com.example.ibrasaloonapp.presentation.theme.Blue
import com.example.ibrasaloonapp.presentation.theme.Red
import com.example.ibrasaloonapp.presentation.ui.Screen
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    onLoggedIn: (AuthData) -> Unit = {},
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val phone = viewModel.state.value.phone
    val phoneError = viewModel.state.value.phoneError
    val code = viewModel.state.value.verifyCode
    val progress = viewModel.uiState.value.progressBarState
    val queue = viewModel.uiState.value.errorQueue
    val showCode = viewModel.state.value.showCode
    val events = viewModel.events

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is LoginViewModel.UIEvent.LoggedIn -> {
//                        navController.navigate(Screen.Home.route) {
//                            popUpTo(Screen.Home.route) { inclusive = true }
//                        }
                        onLoggedIn(event.authData)
                    }
                }
            }
        }
    }




    DefaultScreenUI(
        onRemoveHeadFromQueue = { viewModel.onTriggerEvent(LoginEvent.OnRemoveHeadFromQueue) },
        queue = queue
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

            Text(text = stringResource(id = R.string.lets_start), style = MaterialTheme.typography.h2)
            Text(text = stringResource(id = R.string.the_login_process), style = MaterialTheme.typography.h3)

            Spacer(modifier = Modifier.padding(2.dp))

            AnimatedVisibility(visible = showCode) {
                CodeSection(
                    onTriggerEvent = viewModel::onTriggerEvent,
                    code = code,
                    moveFocus = focusManager::moveFocus
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

@Composable
fun CodeSection(
    moveFocus: (focusDirection: FocusDirection) -> Unit,
    onTriggerEvent: (LoginEvent) -> Unit,
    code: OPT4Digits
) {

    LaunchedEffect(
        key1 = code.one,
    ) {
        if (code.one.isNotEmpty() || code.isEmpty()) {
            moveFocus(FocusDirection.Next)
        }
    }

    LaunchedEffect(
        key1 = code.two,
    ) {
        if (code.two.isNotEmpty()) {
            moveFocus(FocusDirection.Next)
        }
    }

    LaunchedEffect(
        key1 = code.three,
    ) {
        if (code.three.isNotEmpty()) {
            moveFocus(FocusDirection.Next)
        }
    }

    Column(
        horizontalAlignment = CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onTriggerEvent(LoginEvent.Reset) }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back arrow")
            }

            Spacer(modifier = Modifier.padding(4.dp))

            Text(text = stringResource(id = R.string.go_back) , style = MaterialTheme.typography.body1)
        }

        Spacer(modifier = Modifier.padding(4.dp))

        TimeCircularProgressBar(
            animationDuration = 1000 * 60,
            percentage = 1f,
            number = 60,
            color = Red,
            animDelay = 500,
            radius = 26.dp
        )


        Spacer(modifier = Modifier.padding(4.dp))


        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CommonOtpTextField(
                    value = code.one,
                    onTriggerEvent = onTriggerEvent,
                    place = CodeDigitPlace.ONE
                )
                CommonOtpTextField(
                    value = code.two,
                    onTriggerEvent = onTriggerEvent,
                    place = CodeDigitPlace.TWO
                )
                CommonOtpTextField(
                    value = code.three,
                    onTriggerEvent = onTriggerEvent,
                    place = CodeDigitPlace.THREE
                )
                CommonOtpTextField(
                    value = code.four,
                    onTriggerEvent = onTriggerEvent,
                    place = CodeDigitPlace.FOUR
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))

            Row {
                Text(text = stringResource(id = R.string.didnt_receive_code), style = MaterialTheme.typography.caption)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = stringResource(id = R.string.requrest_again), color = Blue, style = MaterialTheme.typography.caption)
            }
        }
    }
}


@Composable
fun CommonOtpTextField(value: String, onTriggerEvent: (LoginEvent) -> Unit, place: CodeDigitPlace) {
    val max = 1
    OutlinedTextField(
        value = value,
        singleLine = true,
        onValueChange = {
            if (it.length <= max) onTriggerEvent(
                LoginEvent.OnCodeDigitChanged(
                    place,
                    it
                )
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = if (place == CodeDigitPlace.FOUR) ImeAction.Done else ImeAction.Next
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(60.dp)
            .height(60.dp),
        maxLines = 1,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        )
    )
}




