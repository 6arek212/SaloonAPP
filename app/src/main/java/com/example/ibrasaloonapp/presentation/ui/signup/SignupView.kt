package com.example.ibrasaloonapp.presentation.ui.signup


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.Gray1
import com.example.ibrasaloonapp.presentation.theme.Green
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace


@Composable
fun SignupView(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel
) {

    val uiMessage = viewModel.uiState.collectAsState().value.uiMessage
    val progress = viewModel.uiState.collectAsState().value.progressBarState

    val image = mainViewModel.state.value.authData?.user?.image
    val showCode = viewModel.state.value.showCode
    val verifyCode = viewModel.state.value.verifyCode
    val pagesNumber = viewModel.pagesNumber
    val page = viewModel.state.value.page
    val firstName = viewModel.state.value.firstName
    val lastName = viewModel.state.value.lastName
    val phone = viewModel.state.value.phone
    val birthDate = viewModel.state.value.birthDate
    val firstNameError = viewModel.state.value.firstNameError
    val lastNameError = viewModel.state.value.lastNameError
    val phoneError = viewModel.state.value.phoneError
    val birthDateError = viewModel.state.value.birthDateError



    DefaultScreenUI(
        uiComponent = uiMessage,
        progressBarState = progress,
        onRemoveUIComponent = { viewModel.onTriggerEvent(SignupEvent.OnRemoveHeadFromQueue) }) {
        Signup(
            isLoading = progress == ProgressBarState.Loading,
            pagesNumber = pagesNumber,
            showCode = showCode,
            page = page,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            imageUrl = image,
            verifyCode = verifyCode,
            birthDate = birthDate,
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            birthDateError = birthDateError,
            phoneError = phoneError,
            onFirstNameChange = { viewModel.onTriggerEvent(SignupEvent.OnFirstNameChanged(it)) },
            onLastNameChange = { viewModel.onTriggerEvent(SignupEvent.OnLastNameChanged(it)) },
            onBirthDateChange = { viewModel.onTriggerEvent(SignupEvent.OnBirthDateChanged(it)) },
            onPhoneChange = { viewModel.onTriggerEvent(SignupEvent.OnPhoneChanged(it)) },
            onNextPage = { viewModel.onTriggerEvent(SignupEvent.NextPage) },
            onPrevPage = { viewModel.onTriggerEvent(SignupEvent.PrevPage) },
            sendAuthVerification = { again ->
                viewModel.onTriggerEvent(
                    SignupEvent.SendAuthVerification(
                        again
                    )
                )
            },
            restPhoneSection = { viewModel.onTriggerEvent(SignupEvent.RestPhoneSection) },
            onCodeDigitChanged = { place, str ->
                viewModel.onTriggerEvent(
                    SignupEvent.OnCodeDigitChanged(
                        place,
                        str
                    )
                )
            },
            onDone = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Signup.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            navigateToUploadImage = {
                navController.navigate(Screen.UploadImage.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}


@Composable
fun Signup(
    verifyCode: OPT4Digits,
    pagesNumber: Int,
    page: Int,
    firstName: String,
    lastName: String,
    phone: String,
    birthDate: String,
    firstNameError: String?,
    lastNameError: String?,
    birthDateError: String?,
    phoneError: String?,
    showCode: Boolean,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onCodeDigitChanged: (CodeDigitPlace, String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit,
    sendAuthVerification: (Boolean) -> Unit,
    onDone: () -> Unit,
    navigateToUploadImage: () -> Unit,
    restPhoneSection: () -> Unit,
    imageUrl: String?,
    isLoading: Boolean
) {

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { focusManager.clearFocus() }
            .background(MaterialTheme.colors.background)
            .padding(8.dp),
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (page in 2 until pagesNumber && page != UPLOAD_PAGE_NUMBER) {
                IconButton(onClick = { onPrevPage() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back arrow")
                }
                Spacer(modifier = Modifier.padding(4.dp))
            }
            SubTitle(text = stringResource(R.string.signup))
        }


        Spacer(modifier = Modifier.padding(12.dp))




        if (page == 1) {
            InformationPage(
                modifier = Modifier.weight(1f),
                firstName = firstName,
                lastName = lastName,
                birthDate = birthDate,
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                birthDateError = birthDateError,
                onFirstNameChange = onFirstNameChange,
                onLastNameChange = onLastNameChange,
                onBirthDateChange = onBirthDateChange
            )
        }


        if (page == 2)
            PhonePage(
                modifier = Modifier.weight(1f),
                moveFocus = focusManager::moveFocus,
                onCodeDigitChanged = onCodeDigitChanged,
                onRest = restPhoneSection,
                showCode = showCode,
                phone = phone,
                phoneError = phoneError,
                onPhoneChange = onPhoneChange,
                sendAuthVerification = sendAuthVerification,
                verifyCode = verifyCode,
                isLoading = isLoading
            )


        if (page == 3) {
            UploadImage(
                modifier = Modifier.weight(1f),
                navigateToUploadImage = navigateToUploadImage,
                imageUrl = imageUrl
            )
        }

        AnimatedVisibility(modifier = Modifier.weight(1f), visible = page == 4) {
            DonePage(modifier = Modifier)
        }

        Spacer(modifier = Modifier.padding(36.dp))



        DotsIndication(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            dotsNumber = pagesNumber,
            dotSize = 12.dp,
            activeDot = page,
            itemsPadding = 4.dp
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Button(
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxWidth(),
            onClick = { if (page == pagesNumber) onDone() else onNextPage() }) {
            Text(
                text = if (page < pagesNumber) stringResource(R.string.next) else stringResource(R.string.done),
                style = MaterialTheme.typography.body1
            )
        }


    }
}


@Composable
fun DotsIndication(
    modifier: Modifier = Modifier,
    dotsNumber: Int,
    dotSize: Dp,
    activeDot: Int,
    itemsPadding: Dp
) {

    if (activeDot > dotsNumber) {
        throw Exception("active dot must be in range 1..dotsNumber")
    }

    if (activeDot < 0 || dotsNumber < 0) {
        throw Exception("dotsNumber , activeDot are non negative numbers")
    }

    Row(modifier) {
        for (i in 1..dotsNumber) {
            Box(
                modifier = Modifier
                    .padding(end = itemsPadding)
                    .clip(CircleShape)
                    .size(dotSize)
                    .background(
                        if (i < activeDot || (i == dotsNumber && activeDot == i)) Green else Gray1
                    )
            )
        }
    }

}


@Composable
fun InformationPage(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    birthDate: String,
    firstNameError: String?,
    lastNameError: String?,
    birthDateError: String?,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit
) {

    val errorModifier = Modifier.padding(top = 2.dp)

    Column(modifier = modifier) {
        Text(text = stringResource(id = R.string.lets_start), style = MaterialTheme.typography.h2)
        Text(
            text = stringResource(id = R.string.the_signup_process),
            style = MaterialTheme.typography.h3
        )

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            label = {
                Text(text = stringResource(id = R.string.first_name))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            onValueChange = { s ->
                onFirstNameChange(s)
            },
            value = firstName,
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Info, "first name")
            },
            textStyle = MaterialTheme.typography.h4
        )

        Text(
            modifier = errorModifier,
            text = firstNameError ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body2
        )


        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            label = {
                Text(text = stringResource(id = R.string.last_name))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            onValueChange = { s ->
                onLastNameChange(s)
            },
            value = lastName,
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Info, "last name")
            },
            textStyle = MaterialTheme.typography.h4
        )

        Text(
            modifier = errorModifier,
            text = lastNameError ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body2
        )


        Spacer(modifier = Modifier.padding(8.dp))

        DatePicker(
            datePicked = birthDate,
            error = birthDateError,
            updatedDate = { str -> onBirthDateChange(str) })

    }
}


@Composable
fun PhonePage(
    modifier: Modifier = Modifier,
    phone: String,
    phoneError: String?,
    onPhoneChange: (String) -> Unit,
    moveFocus: (focusDirection: FocusDirection) -> Unit,
    onCodeDigitChanged: (CodeDigitPlace, String) -> Unit,
    onRest: () -> Unit,
    showCode: Boolean,
    sendAuthVerification: (Boolean) -> Unit,
    verifyCode: OPT4Digits,
    isLoading: Boolean
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.phone_number),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.padding(8.dp))
        AnimatedVisibility(visible = !showCode) {

            Column {
                OutlinedTextField(
                    label = {
                        Text(text = stringResource(id = R.string.phone))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { s ->
                        onPhoneChange(s)
                    },
                    value = phone,
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Filled.Phone, "phone")
                    },
                    textStyle = MaterialTheme.typography.h4
                )

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = phoneError ?: "",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body2
                )

                Spacer(modifier = Modifier.padding(4.dp))

                Button(onClick = { sendAuthVerification(false) }) {
                    Text(text = stringResource(id = R.string.verify))
                }
            }
        }


        AnimatedVisibility(visible = showCode) {
            CommonOtp(
                onChangeFocus = onCodeDigitChanged,
                code = verifyCode,
                moveFocus = moveFocus,
                onRest = onRest,
                showBackButton = true,
                sendAgain = { sendAuthVerification(true) },
                isLoading = isLoading
            )
        }

    }

}


@Composable
fun DonePage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.welcome),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground,
            fontSize = 50.sp
        )

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.to_ibraa_saloon),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.we_are_ready_to_go),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground
        )


        Spacer(modifier = Modifier.padding(16.dp))

        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.barber_logo),
            contentDescription = "logo"
        )
    }
}


@Composable
fun UploadImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    navigateToUploadImage: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularImage(url = imageUrl, modifier = Modifier.size(200.dp), onClick = {})

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.lets_add_a_profile_image),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = navigateToUploadImage,
            colors = ButtonDefaults.buttonColors(backgroundColor = Gray1)
        ) {
            Text(
                text = stringResource(R.string.click_here),
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}


@Composable
@Preview
fun SignupPreview() {
    AppTheme {
        UploadImage(imageUrl = null) {}
    }
}

