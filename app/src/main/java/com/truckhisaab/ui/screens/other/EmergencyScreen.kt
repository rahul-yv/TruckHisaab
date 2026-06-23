package com.truckhisaab.ui.screens.other

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.InfoBlue
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.WarningOrange

@Composable
fun EmergencyScreen(onBack: () -> Unit) {
    Scaffold(topBar = { THTopBar(title = "Emergency SOS", onBack = onBack, containerColor = DangerRed) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(32.dp))
            Box(Modifier.size(120.dp).clip(CircleShape).background(DangerRed).clickable { }, contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, null, tint = Color.White, modifier = Modifier.size(40.dp))
                    Text("SOS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Tap for Emergency Call", fontSize = 14.sp, color = DangerRed, fontWeight = FontWeight.SemiBold)
            Text("Press & hold to send SOS", fontSize = 12.sp, color = TextSecondary)

            Spacer(Modifier.height(32.dp))
            Text("Emergency Numbers", Modifier.fillMaxWidth().padding(horizontal = 16.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))

            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                EmergencyCard(Icons.Default.LocalPolice, "Police", "100", DangerRed)
                EmergencyCard(Icons.Default.LocalHospital, "Ambulance", "108", SuccessGreen)
                EmergencyCard(Icons.Default.Phone, "Highway Helpline", "1033", InfoBlue)
                EmergencyCard(Icons.Default.Build, "Breakdown Assist", "1800-180-1551", WarningOrange)
            }

            Spacer(Modifier.height(24.dp))
            Card(Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = WarningOrange.copy(0.1f))) {
                Column(Modifier.padding(16.dp)) {
                    Text("Emergency Tips", fontWeight = FontWeight.Bold, color = WarningOrange)
                    Spacer(Modifier.height(8.dp))
                    Text("• Pehle apni safety dekho", fontSize = 13.sp)
                    Text("• Truck ko safe jagah par lao", fontSize = 13.sp)
                    Text("• Hazard lights on karo", fontSize = 13.sp)
                    Text("• Nearest thana ya hospital ka pata karo", fontSize = 13.sp)
                    Text("• Insurance company ko call karo", fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun EmergencyCard(icon: ImageVector, label: String, number: String, color: Color) {
    Card(Modifier.fillMaxWidth().clickable { }, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).clip(CircleShape).background(color.copy(0.15f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(number, fontSize = 14.sp, color = color, fontWeight = FontWeight.SemiBold)
            }
            Icon(Icons.Default.Phone, "Call", tint = color, modifier = Modifier.size(28.dp))
        }
    }
}
