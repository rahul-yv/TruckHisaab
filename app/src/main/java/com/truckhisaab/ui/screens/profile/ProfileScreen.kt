package com.truckhisaab.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.ui.components.ConfirmDialog
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.*

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onNotifications: () -> Unit,
    onHelp: () -> Unit,
    onEmergency: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val user = state.user
    var showLogout by remember { mutableStateOf(false) }

    if (showLogout) {
        ConfirmDialog("Logout", "Pakka logout karna hai?", onConfirm = { viewModel.logout { onLogout() } }, onDismiss = { showLogout = false }, confirmColor = DangerRed)
    }

    Scaffold(topBar = { THTopBar(title = "Profile", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            Card(Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = TruckRed)) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(72.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                        Text(user?.name?.firstOrNull()?.uppercase() ?: "U", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TruckRed)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(user?.name ?: "User", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(user?.phone ?: "", color = Color.White.copy(0.8f), fontSize = 14.sp)
                }
            }

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileStat("Trips", "${state.tripCount}", TruckRed, Modifier.weight(1f))
                ProfileStat("Trucks", "${state.truckCount}", InfoBlue, Modifier.weight(1f))
                ProfileStat("Drivers", "${state.driverCount}", SuccessGreen, Modifier.weight(1f))
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
            Text("TruckHisaab v1.0.0", Modifier.fillMaxWidth().padding(16.dp), fontSize = 12.sp, color = TextHint, textAlign = TextAlign.Center)
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
