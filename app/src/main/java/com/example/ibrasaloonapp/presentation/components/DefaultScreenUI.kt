package com.example.ibrasaloonapp.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.theme.Green
import com.example.ibrasaloonapp.presentation.theme.Red
import kotlinx.coroutines.delay


/**
 * @param queue: Dialogs
 * @param content: The content of the UI.
 */
@Composable
fun DefaultScreenUI(
    modifier: Modifier = Modifier,
    uiComponent: UIComponent? = null,
    onRemoveUIComponent: () -> Unit,
    progressBarState: ProgressBarState = ProgressBarState.Idle,
    dialogOnConfirm: () -> Unit = {},
    networkStatus: Boolean? = null,
    onDismissNetworkMessage: (() -> Unit)? = null,
    isSplashRoute: Boolean = false,
    content: @Composable () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent
    ) { padding ->

        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
                .padding(padding)
        ) {
            content()
            //process the queue


            when (uiComponent) {
                is UIComponent.Dialog -> {
                    QuestionDialog(
                        title = uiComponent.title,
                        description = uiComponent.description,
                        onConfirm = {
                            dialogOnConfirm()
                            onRemoveUIComponent()
                        },
                        onDismiss = onRemoveUIComponent,
                        actionButtons = uiComponent.actionButtons
                    )
                }
            }


            if (progressBarState is ProgressBarState.Loading) {
                CircularIndeterminateProgressBar()
            }


            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = networkStatus != null && !isSplashRoute
            ) {
                if (networkStatus != null && !isSplashRoute)
                    TimedLayout(
                        networkStatus = networkStatus,
                        onDismiss = onDismissNetworkMessage
                    )
            }
        }

    }


}

private const val TAG = "DefaultScreenUI"

@Composable
private fun TimedLayout(
    modifier: Modifier = Modifier,
    networkStatus: Boolean,
    onDismiss: (() -> Unit)?
) {

    Log.d(TAG, "TimedLayout: ${networkStatus}")

    LaunchedEffect(key1 = networkStatus) {
        if (networkStatus && onDismiss != null) {
            delay(2000)
            onDismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(if (networkStatus) Green else Red)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (networkStatus) stringResource(id = R.string.network_available) else stringResource(id = R.string.network_not_available),
            color = Gray2,
            style = MaterialTheme.typography.body2
        )
    }
}