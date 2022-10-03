package com.example.ibrasaloonapp.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen
import kotlinx.coroutines.delay


@Composable
fun QuestionDialog2(
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
    }

    Box(modifier = Modifier.width(280.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.background)
                .padding(bottom = 3.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.background),
        ) {
            Column {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "are u sure?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                    Text(
                        text = "stringResource(id = R.string.text_dialog_exit_description)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
                Divider(color = Gray1)
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                // dismiss dialog
//                                navController.popBackStack()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.no),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(Gray1),
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                // go back to home other screen

                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.yes),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun QuestionDialog2Preview() {
    AppTheme {
        QuestionDialog2(
            title = "Do you want to resign?",
            description = "Resigning will make you lose your ratings",
            onConfirm = {},
            onDismiss = {}
        )
    }
}