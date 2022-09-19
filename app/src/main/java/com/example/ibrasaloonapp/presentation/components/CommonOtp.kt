package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.presentation.theme.Blue
import com.example.ibrasaloonapp.presentation.theme.Red
import com.example.ibrasaloonapp.presentation.ui.login.CodeDigitPlace


@Composable
fun CommonOtp(
    modifier: Modifier = Modifier,
    moveFocus: (focusDirection: FocusDirection) -> Unit,
    onChangeFocus: (CodeDigitPlace, String) -> Unit,
    onRest: () -> Unit,
    code: OPT4Digits,
    showBackButton: Boolean = true
) {

    LaunchedEffect(
        key1 = code.one,
    ) {
        if (code.one.isNotEmpty() ) {
            moveFocus(FocusDirection.Next)
        }else{
            moveFocus(FocusDirection.Left)
        }
    }

    LaunchedEffect(
        key1 = code.two,
    ) {
        if (code.two.isNotEmpty()) {
            moveFocus(FocusDirection.Next)
        }else{
            moveFocus(FocusDirection.Left)
        }
    }

    LaunchedEffect(
        key1 = code.three,
    ) {
        if (code.three.isNotEmpty()) {
            moveFocus(FocusDirection.Next)
        }else{
            moveFocus(FocusDirection.Left)
        }
    }

    LaunchedEffect(
        key1 = code.four,
    ) {
        if (code.four.isNotEmpty()) {
            moveFocus(FocusDirection.Next)
        }else{
            moveFocus(FocusDirection.Left)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (showBackButton) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onRest() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back arrow")
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = stringResource(id = R.string.go_back),
                    style = MaterialTheme.typography.body1
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
        }
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
                    onChangeFocus = onChangeFocus,
                    place = CodeDigitPlace.ONE
                )
                CommonOtpTextField(
                    value = code.two,
                    onChangeFocus = onChangeFocus,
                    place = CodeDigitPlace.TWO
                )
                CommonOtpTextField(
                    value = code.three,
                    onChangeFocus = onChangeFocus,
                    place = CodeDigitPlace.THREE
                )
                CommonOtpTextField(
                    value = code.four,
                    onChangeFocus = onChangeFocus,
                    place = CodeDigitPlace.FOUR
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
        }

        Row {
            Text(
                text = stringResource(id = R.string.didnt_receive_code),
                style = MaterialTheme.typography.caption
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = stringResource(id = R.string.requrest_again),
                color = Blue,
                style = MaterialTheme.typography.caption
            )
        }
    }
}


@Composable
private fun CommonOtpTextField(
    value: String,
    onChangeFocus: (CodeDigitPlace, String) -> Unit,
    place: CodeDigitPlace
) {
    val max = 1
    OutlinedTextField(
        value = value,
        singleLine = true,
        onValueChange = {
            if (it.length <= max)
                onChangeFocus(place, it)

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
