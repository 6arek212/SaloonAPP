package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.*
import com.example.ibrasaloonapp.R


@Composable
fun StoryCard(modifier: Modifier = Modifier, image: Int) {
    val painter = rememberAsyncImagePainter(image)
    val workerPainter = rememberAsyncImagePainter(R.drawable.worker1)
    val state = painter.state



    Column(
        modifier = modifier
            .padding(8.dp)
    ) {


        Image(
            modifier = Modifier
                .height(200.dp)
                .width(150.dp)
                .clip(MaterialTheme.shapes.small),
            painter = painter,
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
                    .border(2.dp, Color.White, CircleShape)
                    .shadow(8.dp, CircleShape),
                contentScale = ContentScale.Crop,
                painter = workerPainter,
                contentDescription = ""
            )
        }


    }


}


