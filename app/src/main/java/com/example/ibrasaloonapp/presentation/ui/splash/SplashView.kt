package com.example.ibrasaloonapp.presentation.ui.splash

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ibrasaloonapp.R
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SplashView"

@Composable
fun SplashView(
    navController: NavController,
    authViewModel: MainActivityViewModel = hiltViewModel()
) {

    val state = authViewModel.state.value
    val events = authViewModel.events

    LaunchedEffect(key1 = true) {
        launch {
            events.collect { event ->
                when (event) {
                    is MainActivityViewModel.UIEvent.NavigateNow -> {
                        Log.d(TAG, "SplashView: ${state}")

                        navController.navigate(event.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }


//            navController.navigate(Screen.AppointmentsList.route)

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.barber_shop_brief),
            contentDescription = "",
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(8.dp))
        CircularProgressBar(
            percentage = 1f,
            number = 100,
            color = MaterialTheme.colors.surface,
            animDelay = 500
        )

    }
}


@Composable
fun CircularProgressBar(
    percentage: Float,
    number: Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color: Color = Color.Green,
    strokeWidth: Dp = 8.dp,
    animationDuration: Int = 1000,
    animDelay: Int = 0
) {


    val curPercentage = remember { Animatable(0f) }

    LaunchedEffect(percentage) {
        curPercentage.animateTo(
            percentage,
            animationSpec = tween(durationMillis = animationDuration, delayMillis = animDelay)
        )
    }

    Box(modifier = Modifier.size(radius * 2f), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * curPercentage.value,
                useCenter = false,
                style = Stroke(
                    strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
        Text(
            text = (curPercentage.value * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )

    }
}