package com.example.ibrasaloonapp.presentation.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.theme.White
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CircularImage(
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    url: String? = null,
    uri: Uri? = null,
    onClick: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
//        val imageLoader = ImageLoader.Builder(context)
//            .memoryCache {
//                MemoryCache.Builder(context)
//                    .maxSizePercent(0.25)
//                    .build()
//            }
//            .diskCache {
//                DiskCache.Builder()
//                    .directory(context.cacheDir.resolve("image_cache"))
//                    .maxSizePercent(0.02)
//                    .build()
//            }
//            .build()
//
//        scope.launch {
//            val a = imageLoader.execute(
//                ImageRequest.Builder(context)
//                    .size(Size.ORIGINAL)
//                    .data(url)
//                    .crossfade(true)
//                    .build()
//            )
//            a.request
//        }
    }


//    val painter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(LocalContext.current)
//            .size(Size.ORIGINAL)
//            .data(url)
//            .crossfade(true)
//            .build(),
//        contentScale = ContentScale.Crop,
//        placeholder = painterResource(R.drawable.person_place_holder),
//        error = painterResource(id = R.drawable.error_image_generic),
//        fallback = painterResource(id = R.drawable.person_place_holder)
//    )


    Surface(
        modifier = modifier,
        color = Color.Transparent,
        elevation = elevation,
        shape = CircleShape,
        onClick = onClick
    ) {
//        Image(
//            modifier = Modifier.fillMaxSize(),
//            painter = painter,
//            contentDescription = "",
//            contentScale = ContentScale.Crop,
//        )

        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(2.dp, White, CircleShape),

            model = ImageRequest.Builder(LocalContext.current)
                .data(uri ?: url)
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