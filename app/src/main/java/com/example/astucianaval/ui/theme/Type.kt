package com.example.astucianaval.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.astucianaval.R

val kiranghaeran = FontFamily(
    Font(R.font.kiranghaeran_regular, FontWeight.Normal)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = kiranghaeran
    ),
    titleLarge = TextStyle(
        fontFamily = kiranghaeran,
        fontWeight = FontWeight.Bold
    ),
    labelLarge = TextStyle(
        fontFamily = kiranghaeran
    )
)
