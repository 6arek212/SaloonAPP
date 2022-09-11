package com.example.ibrasaloonapp.presentation.components

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.theme.Green
import com.example.ibrasaloonapp.presentation.theme.Red

private const val TAG = "ImageChip"

@Composable
fun VerticalImageChip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    url: String? = null,
    imageSize: Dp = 45.dp
) {


    Column(modifier = modifier.width(IntrinsicSize.Max)) {

        CircularImage(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .zIndex(1f)
                .size(imageSize)
                .offset(y = 15.dp),
            elevation = 8.dp,
            url = if (url != null) "https://saloon-ibra-api.herokuapp.com/imgs/${url}" else null,
        )

        CustomChip(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            contentPaddingValues = PaddingValues(
                start = 8.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 6.dp
            ),
            text = text,
            onClick = onClick,
            isSelected = isSelected
        )

    }


}