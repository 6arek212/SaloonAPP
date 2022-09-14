package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ibrasaloonapp.presentation.theme.Gray2


@Composable
fun SubTitle(modifier: Modifier = Modifier, text: String) {

    ConstraintLayout(modifier = modifier) {
        val (textRef, div) = createRefs()

        Text(
            modifier = Modifier.constrainAs(textRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            },
            text = text,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h3
        )

        Divider(
            modifier = Modifier
                .padding(top = 4.dp)
                .height(2.dp)
                .clip(MaterialTheme.shapes.large)
                .background(Gray2)
                .constrainAs(div) {
                    start.linkTo(textRef.start)
                    end.linkTo(textRef.end)
                    top.linkTo(textRef.bottom)
                    width = Dimension.fillToConstraints
                }
        )
    }


}