package com.example.ibrasaloonapp.presentation.ui.edit_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace
import com.example.ibrasaloonapp.presentation.ui.upload.EditProfileUIEvent
import kotlinx.coroutines.flow.collect


@Composable
fun ChangePhoneNumberView(
    navController: NavController,
    editProfileViewModel: EditProfileViewModel
) {
    val phone = editProfileViewModel.state.value.phone
    val phoneError = editProfileViewModel.state.value.phoneError
    val code = editProfileViewModel.state.value.code
    val showCode = editProfileViewModel.state.value.showCode
    val progress = editProfileViewModel.uiState.collectAsState().value.progressBarState
    val message = editProfileViewModel.uiState.collectAsState().value.uiMessage
    val events = editProfileViewModel.events
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is EditProfileUIEvent.PhoneUpdated -> {
                    focusManager.clearFocus()
                    navController.popBackStack()
                }
                else -> {}
            }
        }
    }


    DefaultScreenUI(
        progressBarState = progress,
        uiComponent = message,
        onRemoveUIComponent = { editProfileViewModel.onTriggerEvent(EditProfileEvent.OnRemoveHeadFromQueue) }) {
        ChangePhoneNumber(
            phoneError = phoneError,
            showCode = showCode,
            phone = phone,
            code = code,
            onPhoneChanged = { s ->
                editProfileViewModel.onTriggerEvent(EditProfileEvent.OnPhoneChanged(s))
            },
            sendAuthVerification = { again ->
                editProfileViewModel.onTriggerEvent(
                    EditProfileEvent.SendAuthVerification(
                        again
                    )
                )
            },
            onCodeDigitChanged = { place, v ->
                editProfileViewModel.onTriggerEvent(
                    EditProfileEvent.OnCodeDigitChanged(
                        place,
                        v
                    )
                )
            },
            onRestCode = { editProfileViewModel.onTriggerEvent(EditProfileEvent.OnRestCode) },
            clearFocus = focusManager::clearFocus,
            moveFocus = focusManager::moveFocus,
            close = navController::popBackStack,
            isLoading = progress == ProgressBarState.Loading
        )
    }

}


@Composable
fun ChangePhoneNumber(
    showCode: Boolean,
    phone: String,
    phoneError: String?,
    code: OPT4Digits,
    onPhoneChanged: (String) -> Unit,
    sendAuthVerification: (Boolean) -> Unit,
    onCodeDigitChanged: (CodeDigitPlace, String) -> Unit,
    onRestCode: () -> Unit,
    moveFocus: (focusDirection: FocusDirection) -> Unit,
    clearFocus: () -> Unit,
    close: () -> Unit,
    isLoading: Boolean
) {
    val scrollState = rememberScrollState()
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
            .verticalScroll(scrollState)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { clearFocus() }
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            BackButton(onClick = close, tint = MaterialTheme.colors.primary)

            Spacer(modifier = Modifier.padding(4.dp))

            SubTitle(text = stringResource(R.string.change_phone_number))
        }



        Spacer(modifier = Modifier.padding(16.dp))



        AnimatedVisibility(visible = !showCode) {
            Column() {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(id = R.string.phone))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { s ->
                        onPhoneChanged(s)
                    },
                    value = phone,
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Filled.Phone, stringResource(id = R.string.phone))
                    },
                    textStyle = MaterialTheme.typography.h4
                )

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = phoneError ?: "",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body2
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    onClick = { sendAuthVerification(false) }
                ) {
                    Text(text = stringResource(id = R.string.verify))
                }

            }
        }


        AnimatedVisibility(visible = showCode) {
            CommonOtp(
                moveFocus = moveFocus,
                onChangeFocus = onCodeDigitChanged,
                onRest = onRestCode,
                code = code,
                sendAgain = { sendAuthVerification(true) },
                isLoading = isLoading
            )
        }
    }

}


@Composable
@Preview
fun ChangePhoneNumberPreview() {
    AppTheme {
        ChangePhoneNumber(
            phone = "",
            code = OPT4Digits(""),
            onPhoneChanged = {},
            sendAuthVerification = {},
            onCodeDigitChanged = { a, s -> },
            showCode = false,
            onRestCode = {},
            phoneError = null,
            clearFocus = {},
            moveFocus = {},
            close = {},
            isLoading = false
        )
    }
}