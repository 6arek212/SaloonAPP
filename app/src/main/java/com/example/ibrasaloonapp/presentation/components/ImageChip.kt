package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.ibrasaloonapp.R


@Composable
fun ImageChip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {

    Row(modifier = modifier) {

        Surface(
            elevation = 10.dp,
            modifier = Modifier
                .zIndex(1f)
                .size(45.dp)
                .offset(15.dp)
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = CircleShape
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .shadow(8.dp, CircleShape),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.woker1),
                contentDescription = ""
            )
        }


        CustomChip(
            modifier = modifier,
            contentPaddingValues = PaddingValues(
                start = 24.dp,
                top = 6.dp,
                end = 8.dp,
                bottom = 6.dp
            ),
            text = text,
            onClick = onClick,
            isSelected = isSelected
        )

    }


}