package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ibrasaloonapp.presentation.theme.RedLite


@Composable
fun DateTimeCard(
    modifier: Modifier = Modifier,
    date: String = "August 08, 2022",
    time: String = "12:00",
    caption: String = "1 hour to you'r appointment"
) {
    Column(modifier = modifier) {

        ConstraintLayout(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.medium)
                .background(RedLite)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            val (dateRef, timeRef, captionRef) = createRefs()

            Text(
                modifier = Modifier.constrainAs(dateRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
                text = date,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )


            Text(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .constrainAs(timeRef) {
                        start.linkTo(dateRef.start)
                        top.linkTo(dateRef.bottom)
                    },
                text = time,
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onSurface
            )
        }

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = caption,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )
    }


}