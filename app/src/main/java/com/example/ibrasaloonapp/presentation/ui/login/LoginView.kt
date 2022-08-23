package com.example.ibrasaloonapp.presentation.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.ui.Screen
import kotlinx.coroutines.launch

@Composable
fun LoginView(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {

    val phone = viewModel.state.value.phone
    val password = viewModel.state.value.password
    val phoneError = viewModel.state.value.phoneError
    val passwordError = viewModel.state.value.passwordError
    val progress = viewModel.state.value.progressBarState
    val queue = viewModel.state.value.errorQueue
    val events = viewModel.events

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)



    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is LoginViewModel.UIEvent.LoggedIn -> {
                        navController.navigate(Screen.AppointmentsList.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }




    DefaultScreenUI(
        onRemoveHeadFromQueue = { viewModel.onTriggerEvent(LoginEvent.OnRemoveHeadFromQueue) },
        queue = queue
    ) {

        Box(modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { focusManager.clearFocus() }
        )) {

            Surface(
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxSize()
            ) {}

            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier
                    .fillMaxHeight(.9f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(bottomEnd = 60.dp, bottomStart = 60.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(R.drawable.barber_shop_brief),
                        contentDescription = "",
                        modifier = Modifier
                            .height(200.dp)
                            .width(200.dp)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    TextFields(
                        modifier = modifier,
                        phone = phone,
                        password = password,
                        onTriggerEvent = viewModel::onTriggerEvent,
                        phoneError = phoneError,
                        passwordError = passwordError
                    )

                    Spacer(modifier = Modifier.padding(vertical = 12.dp))

                    Text("Forgot password?", textAlign = TextAlign.End, modifier = modifier)

                    Spacer(modifier = Modifier.padding(vertical = 16.dp))

                    Bottom(
                        onTriggerEvent = viewModel::onTriggerEvent,
                        modifier = modifier,
                        progressBar = progress
                    )

                }
            }

        }
    }

}


@Composable
private fun TextFields(
    modifier: Modifier,
    phone: String,
    password: String,
    phoneError: String?,
    passwordError: String?,
    onTriggerEvent: (LoginEvent) -> Unit,
) {

    val passwordVisibleState = remember {
        mutableStateOf(false)
    }

    Column {

        OutlinedTextField(
            modifier = modifier,
            label = {
                Text(text = "Phone")
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
                Icon(Icons.Filled.Email, "email")
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



        Spacer(modifier = Modifier.padding(6.dp))


        OutlinedTextField(
            modifier = modifier,
            value = password,
            onValueChange = { s -> onTriggerEvent(LoginEvent.OnPasswordValueChange(s)) },
            label = {
                Text(text = "Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation = if (passwordVisibleState.value) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibleState.value = !passwordVisibleState.value
                }) {
                    Icon(
                        imageVector = if (passwordVisibleState.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        ""
                    )
                }
            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Lock, contentDescription = "password")
            },
            textStyle = MaterialTheme.typography.h4
        )

        passwordError?.let {
            Text(
                modifier = modifier,
                text = passwordError,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2
            )
        }

    }
}


@Composable
private fun Bottom(
    onTriggerEvent: (LoginEvent) -> Unit,
    progressBar: ProgressBarState = ProgressBarState.Idle,
    modifier: Modifier
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
                onClick = { onTriggerEvent(LoginEvent.Login) },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                ),
                modifier = modifier,
                contentPadding = PaddingValues(all = 16.dp),
            ) {
                Text(text = "Log In")
            }
        }


    }


}