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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.theme.White


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


//        AsyncImage(
//            modifier = Modifier
//                .height(200.dp)
//                .width(150.dp)
//                .clip(MaterialTheme.shapes.small),
//
//            model = ImageRequest.Builder(LocalContext.current)
//                .placeholderMemoryCacheKey("123")
//                .size(size = 100)
//                .scale(Scale.FILL)
//                .data("https://saloon-ibra-api.herokuapp.com/imgs/6317b6ded417e4cbffde4da5.jpg")
//                .crossfade(true)
//                .build(),
//            placeholder = painterResource(R.drawable.person_place_holder),
//            contentDescription = "",
//            contentScale = ContentScale.Crop,
//            error = painterResource(id = R.drawable.error_image_generic),
//            fallback = painterResource(id = R.drawable.person_place_holder),
//
//            )
//
//
//
//
//        CircularImage(
//            url = "https://saloon-ibra-api.herokuapp.com/imgs/6317b6ded417e4cbffde4da5.jpg",
//            modifier = Modifier
//                .size(64.dp)
//                .align(Alignment.CenterHorizontally)
//                .offset(y = -15.dp)
//        )

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
                painter = painterResource(id = R.drawable.woker1),
                contentDescription = ""
            )
        }


    }


}


