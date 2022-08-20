import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@ExperimentalMaterialApi
@Composable
fun DropDownMenuComponent(
    error: String? = null,
    label: String = "",
    expanded: Boolean,
    selectedOptionText: String,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        Column {

            OutlinedTextField(
                modifier = Modifier,
                readOnly = true,
                value = selectedOptionText,
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
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
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

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            content()
        }
    }

}