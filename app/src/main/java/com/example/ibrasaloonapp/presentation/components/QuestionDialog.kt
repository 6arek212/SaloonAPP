package com.example.ibrasaloonapp.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.Black1
import com.example.ibrasaloonapp.presentation.theme.Black2
import com.example.ibrasaloonapp.presentation.theme.Black4
import com.example.ibrasaloonapp.presentation.ui.Screen
import kotlinx.coroutines.delay


@Composable
fun QuestionDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    actionButtons: Boolean = false,
    buttonConfirmText: String = stringResource(R.string.yes),
    buttonNegativeText: String = stringResource(R.string.no),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    val duration = 300
    val interactionSource = remember { MutableInteractionSource() }
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f, animationSpec = tween(
            durationMillis = duration
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(duration.toLong())
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onDismiss() }
            .alpha(alpha = alphaAnim.value),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(listOf(Black2, Black2)), alpha = .95f)
                .blur(30.dp)
        )

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 18.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = modifier
                    .heightIn(min = 150.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Column (modifier = Modifier.align(Alignment.Start)){
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.body2,
                        color = Black4,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400
                    )
                }

                if (actionButtons) {

                    Spacer(modifier = Modifier.padding(16.dp))



                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onConfirm,
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor =
                            MaterialTheme.colors.surface
                        ),
                    ) {
                        Text(
                            text = buttonConfirmText,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    ) {
                        Text(
                            text = buttonNegativeText,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.background
                        )
                    }
                }
            }
        }
    }

}


@Composable
@Preview
fun QuestionDialogPreview() {
    AppTheme {
        QuestionDialog(
            title = "Do you want to resign?",
            description = "Resigning will make you lose your ratings",
            onConfirm = {},
            onDismiss = {}
        )
    }
}