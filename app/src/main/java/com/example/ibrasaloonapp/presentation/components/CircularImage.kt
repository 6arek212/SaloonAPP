package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.theme.White

@Composable
fun CircularImage(modifier: Modifier = Modifier, elevation: Dp = 2.dp, url: String?) {

    Surface(
        modifier = modifier,
        color = Color.Transparent,
        elevation = elevation,
        shape = CircleShape
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(2.dp, White, CircleShape),

            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.person_place_holder),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.error_image_generic),
            fallback = painterResource(id = R.drawable.person_place_holder),

            )
    }

}