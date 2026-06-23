package com.truckhisaab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TruckHisaabColorScheme = lightColorScheme(
    primary = TruckRed,
    onPrimary = Color.White,
    primaryContainer = TruckRedLight,
    onPrimaryContainer = Color.White,
    secondary = TextPrimary,
    onSecondary = Color.White,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = TextSecondary,
    error = DangerRed,
    onError = Color.White,
    outline = Divider,
    outlineVariant = Color(0xFFEEEEEE)
)

@Composable
fun TruckHisaabTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TruckHisaabColorScheme,
        typography = Typography,
        content = content
    )
}
