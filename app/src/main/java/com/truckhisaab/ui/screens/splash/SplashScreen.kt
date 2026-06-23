package com.truckhisaab.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.theme.TruckRed
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigate: () -> Unit) {
    val scale = remember { Animatable(0.3f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, tween(800, easing = FastOutSlowInEasing))
    }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, tween(1000))
        delay(1500)
        onNavigate()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(TruckRed),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.LocalShipping,
                contentDescription = "TruckHisaab",
                modifier = Modifier.size(100.dp).scale(scale.value).alpha(alpha.value),
                tint = Color.White
            )
            Spacer(Modifier.height(20.dp))
            Text(
                "TruckHisaab",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(alpha.value)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Truck ka hisaab, ab aasaan",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.alpha(alpha.value)
            )
            Spacer(Modifier.height(32.dp))
            LoadingDots(alpha.value)
        }
    }
}

@Composable
private fun LoadingDots(parentAlpha: Float) {
    val dot1 = remember { Animatable(0.3f) }
    val dot2 = remember { Animatable(0.3f) }
    val dot3 = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        while (true) {
            dot1.animateTo(1f, tween(300)); dot1.animateTo(0.3f, tween(300))
            dot2.animateTo(1f, tween(300)); dot2.animateTo(0.3f, tween(300))
            dot3.animateTo(1f, tween(300)); dot3.animateTo(0.3f, tween(300))
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.alpha(parentAlpha)
    ) {
        listOf(dot1, dot2, dot3).forEach { anim ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(anim.value)
                    .background(Color.White, CircleShape)
            )
            Spacer(Modifier.width(8.dp))
        }
    }
}
