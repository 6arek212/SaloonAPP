import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
    val rotation = animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, animationSpec = tween(
            durationMillis = 500
        )
    )

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
                    Icon(
                        modifier = Modifier.rotate(rotation.value),
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "ArrowDropDown"
                    )
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