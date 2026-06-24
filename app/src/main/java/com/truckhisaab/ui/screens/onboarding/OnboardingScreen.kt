package com.truckhisaab.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.TextSecondary
import kotlinx.coroutines.launch

private data class Page(val icon: ImageVector, val title: String, val subtitle: String)
private val pages = listOf(
    Page(Icons.AutoMirrored.Filled.Chat, "WhatsApp pe hisaab", "Koi app download nahi. Bas message karo aur sab track ho jayega."),
    Page(Icons.Default.LocalShipping, "Trip track karo", "Route, cargo, freight - sab log karo. Kharcha bhi yahi add karo."),
    Page(Icons.Default.Assessment, "P&L dekho", "Aaj, hafte, mahine ka hisaab instant dekhein. PDF aur WhatsApp pe share karein.")
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize().background(Color.White).padding(24.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onFinish) { Text("Skip", color = TextSecondary) }
        }
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            val p = pages[page]
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(140.dp).clip(CircleShape).background(TruckRed.copy(0.1f)), contentAlignment = Alignment.Center) {
                    Icon(p.icon, null, Modifier.size(72.dp), tint = TruckRed)
                }
                Spacer(Modifier.height(40.dp))
                Text(p.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Text(p.subtitle, fontSize = 16.sp, color = TextSecondary, textAlign = TextAlign.Center, lineHeight = 24.sp)
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(pages.size) { i ->
                Box(Modifier.padding(4.dp).size(if (pagerState.currentPage == i) 10.dp else 8.dp).clip(CircleShape).background(if (pagerState.currentPage == i) TruckRed else Color.LightGray))
            }
        }
        Spacer(Modifier.height(24.dp))
        THPrimaryButton(
            text = if (pagerState.currentPage == pages.size - 1) "Shuru Karein" else "Aage",
            onClick = { if (pagerState.currentPage == pages.size - 1) onFinish() else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
    }
}
