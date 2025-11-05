package com.example.astucianaval.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SoftBlueColorScheme = darkColorScheme(
    primary = SoftBluePrimary,
    secondary = SoftBlueSecondary,
    tertiary = SoftBlueTertiary,
    background = SoftBlueBackground,
    surface = SoftBlueSurface,
    onPrimary = SoftBlueOnPrimary,
    onBackground = SoftBlueOnBackground
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SoftBlueColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}