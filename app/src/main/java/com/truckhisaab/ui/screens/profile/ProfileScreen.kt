package com.truckhisaab.ui.screens.profile

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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.ui.components.ConfirmDialog
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.InfoBlue
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.WarningOrange

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onNotifications: () -> Unit,
    onHelp: () -> Unit,
    onEmergency: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var showLogout by remember { mutableStateOf(false) }

    if (showLogout) {
        ConfirmDialog("Logout", "Pakka logout karna hai?", onConfirm = { onLogout() }, onDismiss = { showLogout = false }, confirmColor = DangerRed)
    }

    Scaffold(topBar = { THTopBar(title = "Profile", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            Card(Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = TruckRed)) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(72.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                        Text(state.name.firstOrNull()?.uppercase() ?: "U", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TruckRed)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(state.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(state.phone, color = Color.White.copy(0.8f), fontSize = 14.sp)
                    Text(state.role, color = Color.White.copy(0.7f), fontSize = 13.sp)
                }
            }

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileStat("Trips", "${state.totalTrips}", TruckRed, Modifier.weight(1f))
                ProfileStat("Income", formatINR(state.totalIncome), SuccessGreen, Modifier.weight(1f))
                ProfileStat("Trucks", "${state.totalTrucks}", InfoBlue, Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column {
                    MenuItem(Icons.Default.Settings, "Settings", onClick = onSettings)
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    MenuItem(Icons.Default.Notifications, "Notifications", onClick = onNotifications)
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    MenuItem(Icons.Default.Warning, "Emergency SOS", onClick = onEmergency, tint = DangerRed)
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    MenuItem(Icons.AutoMirrored.Filled.Help, "Help / Madad", onClick = onHelp)
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    MenuItem(Icons.Default.Share, "App Share Karein", onClick = { })
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    MenuItem(Icons.Default.Star, "Rate App", onClick = { })
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    MenuItem(Icons.AutoMirrored.Filled.Logout, "Logout", onClick = { showLogout = true }, tint = DangerRed)
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("TruckHisaab v1.0.0", Modifier.fillMaxWidth().padding(16.dp), fontSize = 12.sp, color = TextHint, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String, color: Color, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = color)
            Text(label, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun MenuItem(icon: ImageVector, label: String, onClick: () -> Unit, tint: Color = TextSecondary) {
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp, 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, Modifier.weight(1f), fontSize = 15.sp, fontWeight = FontWeight.Medium, color = if (tint == DangerRed) DangerRed else Color.Unspecified)
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, tint = TextHint, modifier = Modifier.size(14.dp))
    }
}
