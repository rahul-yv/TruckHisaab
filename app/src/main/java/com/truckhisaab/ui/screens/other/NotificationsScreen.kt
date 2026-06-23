package com.truckhisaab.ui.screens.other

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.data.AppContainer
import com.truckhisaab.data.model.NotificationType
import com.truckhisaab.ui.components.EmptyState
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatTimeAgo
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.InfoBlue
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.WarningOrange

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val notifications by AppContainer.userRepository.notifications.collectAsState()

    Scaffold(topBar = { THTopBar(title = "Notifications", onBack = onBack) }) { padding ->
        if (notifications.isEmpty()) {
            EmptyState(icon = Icons.Default.Notifications, title = "Koi notification nahi", modifier = Modifier.fillMaxSize().padding(padding))
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notifications) { notif ->
                    val (icon, color) = when (notif.type) {
                        NotificationType.TRIP -> Icons.Default.LocalShipping to TruckRed
                        NotificationType.EXPENSE -> Icons.Default.Receipt to WarningOrange
                        NotificationType.DOCUMENT -> Icons.Default.Description to DangerRed
                        NotificationType.REPORT -> Icons.Default.Assessment to SuccessGreen
                        NotificationType.SYSTEM -> Icons.Default.Notifications to InfoBlue
                    }
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = if (!notif.isRead) color.copy(0.05f) else Color.White)) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(40.dp).clip(CircleShape).background(color.copy(0.15f)), contentAlignment = Alignment.Center) {
                                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(notif.title, fontWeight = if (!notif.isRead) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
                                Text(notif.message, fontSize = 12.sp, color = TextSecondary, maxLines = 2)
                                Text(formatTimeAgo(notif.timestamp), fontSize = 11.sp, color = TextHint)
                            }
                            if (!notif.isRead) {
                                Box(Modifier.size(8.dp).clip(CircleShape).background(color))
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
