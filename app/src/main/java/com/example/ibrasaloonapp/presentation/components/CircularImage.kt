package com.example.ibrasaloonapp.presentation.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.theme.White


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CircularImage(
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    url: String? = null,
    uri: Uri? = null,
    onClick: () -> Unit
) {

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri ?: url)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.person_place_holder),
        error = painterResource(id = R.drawable.error_image_generic),
        fallback = painterResource(id = R.drawable.person_place_holder)
    )


    Surface(
        modifier = modifier,
        elevation = elevation,
        shape = CircleShape,
        onClick = onClick
    ) {
        Box {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, White, CircleShape),
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )

            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is AsyncImagePainter.State.Error -> {

                }
            }
        }

    }

}