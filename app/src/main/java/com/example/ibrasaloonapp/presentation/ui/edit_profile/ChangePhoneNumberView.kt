package com.example.ibrasaloonapp.presentation.ui.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.OPT4Digits
import com.example.ibrasaloonapp.presentation.components.CommonOtp
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.theme.AppTheme


@Composable
fun ChangePhoneNumberView(editProfileViewModel: EditProfileViewModel) {
    val phone = editProfileViewModel.state.value.phone


    ChangePhoneNumber(
        phone = phone,
        onPhoneChanged = { s ->
            editProfileViewModel.onTriggerEvent(EditProfileEvent.OnPhoneChanged(s))
        }
    )
}


@Composable
fun ChangePhoneNumber(phone: String, onPhoneChanged: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        SubTitle(text = "Change Phone Number")

        Spacer(modifier = Modifier.padding(16.dp))



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
                onPhoneChanged(s)
            },
            value = phone,
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Phone, stringResource(id = R.string.phone))
            },
            textStyle = MaterialTheme.typography.h4
        )



        CommonOtp(
            moveFocus = {},
            onChangeFocus = { place, str -> },
            onRest = { /*TODO*/ },
            code = OPT4Digits("")
        )

    }

}


@Composable
@Preview
fun ChangePhoneNumberPreview() {
    AppTheme {
        ChangePhoneNumber("05251455655", {})
    }
}