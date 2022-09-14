package com.example.ibrasaloonapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.core.domain.UIComponent


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
        }

    }


}