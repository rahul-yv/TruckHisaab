package com.truckhisaab.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.util.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigate: () -> Unit) {
    val scale = remember { Animatable(0.5f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        delay(Constants.SPLASH_DELAY)
        onNavigate()
    }

    Box(Modifier.fillMaxSize().background(TruckRed), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.LocalShipping, null, modifier = Modifier.size(120.dp).scale(scale.value), tint = Color.White)
            Spacer(Modifier.height(16.dp))
            Text("TruckHisaab", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))
            Text("Truck ka hisaab, ab aasaan", fontSize = 16.sp, color = Color.White.copy(0.8f))
        }
        Text("v${Constants.APP_VERSION}", Modifier.align(Alignment.BottomCenter).padding(24.dp), color = Color.White.copy(0.5f), fontSize = 12.sp)
    }
}
