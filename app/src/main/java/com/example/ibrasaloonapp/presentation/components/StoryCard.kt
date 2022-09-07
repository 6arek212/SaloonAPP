package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.theme.Gray2


@Composable
fun StoryCard(modifier: Modifier = Modifier, image: Int) {
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        Image(
            modifier = Modifier
                .height(200.dp)
                .width(150.dp)
                .clip(MaterialTheme.shapes.small),
            painter = painterResource(id = image),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )


        Surface(
            elevation = 10.dp,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally)
                .offset(y = -15.dp),
            shape = CircleShape,
            color = Color.Transparent
        ) {

            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, Gray2, CircleShape)
                    .shadow(8.dp, CircleShape),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.woker1),
                contentDescription = ""
            )
        }


    }


}


