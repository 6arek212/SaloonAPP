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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.MainEvent
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.components.CommonOtp
import com.example.ibrasaloonapp.presentation.components.DatePicker
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.theme.Gray1
import com.example.ibrasaloonapp.presentation.theme.Green
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace
import kotlinx.coroutines.launch


@Composable
fun SignupView(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel
) {

//    val duration = 1000
//    var startAnimation by remember {
//        mutableStateOf(false)
//    }
//    val alphaAnim = animateFloatAsState(
//        targetValue = if (startAnimation) 1f else 0f, animationSpec = tween(
//            durationMillis = duration
//        )
//    )
    val uiMessage = viewModel.uiState.value.uiMessage
    val progress = viewModel.uiState.value.progressBarState

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
    val events = viewModel.uiEvents


    LaunchedEffect(Unit) {
        launch {
            events.collect { event ->
                when (event) {
                    is MainUIEvent.LoggedIn -> {
                        mainViewModel.onTriggerEvent(
                            MainEvent.Login(
                                event.authData
                            )
                        )
                    }
                }
            }
        }
    }

    DefaultScreenUI(
        uiComponent = uiMessage,
        progressBarState = progress,
        onRemoveUIComponent = { viewModel.onTriggerEvent(SignupEvent.OnRemoveHeadFromQueue) }) {
        Signup(
            pagesNumber = pagesNumber,
            showCode = showCode,
            page = page,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
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
            sendAuthVerification = { viewModel.onTriggerEvent(SignupEvent.SendAuthVerification) },
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
    sendAuthVerification: () -> Unit,
    onDone: () -> Unit
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
            if (page in 2 until pagesNumber) {
                IconButton(onClick = { onPrevPage() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back arrow")
                }
                Spacer(modifier = Modifier.padding(4.dp))
            }
            SubTitle(text = "Signup")
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

//        AnimatedVisibility(modifier = Modifier.weight(1f), visible = page == 2) {

        if (page == 2)
            PhonePage(
                modifier = Modifier.weight(1f),
                moveFocus = focusManager::moveFocus,
                onCodeDigitChanged = onCodeDigitChanged,
                onRest = {},
                showCode = showCode,
                phone = phone,
                phoneError = phoneError,
                onPhoneChange = onPhoneChange,
                sendAuthVerification = sendAuthVerification,
                verifyCode = verifyCode
            )

//        }

        AnimatedVisibility(modifier = Modifier.weight(1f), visible = page == 3) {
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
            modifier = Modifier.fillMaxWidth(),
            onClick = { if (page == pagesNumber) onDone() else onNextPage() }) {
            Text(
                text = if (page < pagesNumber) "Next" else "Done",
                style = MaterialTheme.typography.body2
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
    sendAuthVerification: () -> Unit,
    verifyCode: OPT4Digits
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
            Row {

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
                }

                Button(onClick = { sendAuthVerification() }) {
                    Text(text = "Verify")
                }
            }
        }


        AnimatedVisibility(visible = showCode) {
            CommonOtp(
                onChangeFocus = onCodeDigitChanged,
                code = verifyCode,
                moveFocus = moveFocus,
                onRest = onRest,
                showBackButton = true
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


//@Composable
//@Preview
//fun SignupPreview() {
//    AppTheme {
//        Signup(
//            firstName = "tarik",
//            lastName = "husin",
//            phone = "0525145565",
//            birthDate = "10/06/1998",
//            firstNameError = null,
//            lastNameError = null,
//            birthDateError = null,
//            phoneError = null,
//            onFirstNameChange = {},
//            onLastNameChange = {},
//            onBirthDateChange = {},
//            onPhoneChange = {},
//            onNextPage = {},
//            page = 1
//        )
//    }
//}

