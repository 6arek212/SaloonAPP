package com.example.ibrasaloonapp.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun TimeCircularProgressBar(
    modifier: Modifier = Modifier,
    percentage: Float,
    number: Int,
    radius: Dp = 40.dp,
    color: Color = Color.Green,
    strokeWidth: Dp = 4.dp,
    animationDuration: Int = 1000,
    animDelay: Int = 0,
    timesUpCallback: () -> Unit = {}
) {

    val ticks = remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (ticks.value < number) {
            delay(1.seconds)
            ticks.value++
        }
        timesUpCallback()
    }

//    val curPercentage = remember { Animatable(0f) }
//
//    LaunchedEffect(percentage) {
//        curPercentage.animateTo(
//            percentage,
//            animationSpec = tween(durationMillis = animationDuration, delayMillis = animDelay)
//        )
//    }


    Column() {
        Box(modifier = Modifier.size(radius * 2f), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(radius * 2f)) {
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360 * ticks.value.toFloat() / number,
                    useCenter = false,
                    style = Stroke(
                        strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }

            Text(
                text = "${ticks.value} sec",
                color = Color.Black,
                style = MaterialTheme.typography.body2
            )

        }
    }
}