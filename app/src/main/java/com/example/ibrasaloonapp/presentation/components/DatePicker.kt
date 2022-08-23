package com.example.ibrasaloonapp.presentation.components

import android.icu.text.MessageFormat.format
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.lang.String.format
import java.text.DateFormat
import java.text.MessageFormat.format
import java.text.SimpleDateFormat
import java.util.*


private fun showDatePicker(
    activity: FragmentActivity,
    updatedDate: (String) -> Unit
) {
    val builder = MaterialDatePicker.Builder.datePicker()

    val dateValidator: DateValidator = DateValidatorPointForward.now()

    val constraintsBuilder = CalendarConstraints.Builder().setValidator(dateValidator)

    builder.setCalendarConstraints(constraintsBuilder.build())
    val date = Date().time

    val picker = builder.setSelection(date).build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        val dateString: String =
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
        updatedDate(dateString)
    }
}


@Composable
fun DatePicker(
    label: String = "",
    error: String? = null,
    datePicked: String,
    updatedDate: (String) -> Unit,
) {
    val activity = LocalContext.current as FragmentActivity


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        OutlinedTextField(
            modifier = Modifier.clickable {
                showDatePicker(activity, updatedDate)
            },
            enabled = false,
            readOnly = true,
            value = datePicked,
            onValueChange = { },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
            ),
            singleLine = true,
            trailingIcon = {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
            },
            isError = error != null
        )


        error?.let {
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2
            )
        }

    }

}