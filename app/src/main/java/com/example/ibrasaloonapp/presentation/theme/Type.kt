package com.example.ibrasaloonapp.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ibrasaloonapp.R

private val Poppins_Font = FontFamily(
    Font(R.font.poppins_thin, FontWeight.W100),
    Font(R.font.poppins_extralight, FontWeight.W200),
    Font(R.font.poppins_light, FontWeight.W300),
    Font(R.font.poppins_regular, FontWeight.W400),
    Font(R.font.poppins_medium, FontWeight.W500),
    Font(R.font.poppins_semibold, FontWeight.W600),
    Font(R.font.poppins_bold, FontWeight.W700),
    Font(R.font.poppins_extrabold, FontWeight.W800),
    )


// Set of Material typography styles to start with
val SFTypography = Typography(
    h1 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W800,
        fontSize = 30.sp,
    ),
    h2 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
    ),
    h3 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
    ),
    h4 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
    ),
    h5 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
    ),
    h6 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W300,
        fontSize = 14.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    subtitle2 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    body1 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight =  FontWeight.W600,
        fontSize = 12.sp
    ),
    button = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W300,
        fontSize = 16.sp,
        color = Gray1
    ),
    caption = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W200,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = Poppins_Font,
        fontWeight = FontWeight.W300,
        fontSize = 26.sp
    )
)