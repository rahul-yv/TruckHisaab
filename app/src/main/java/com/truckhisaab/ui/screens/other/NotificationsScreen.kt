package com.truckhisaab.ui.screens.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.AppNotification
import com.truckhisaab.domain.model.NotificationType
import com.truckhisaab.domain.repository.NotificationRepository
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepo: NotificationRepository
) : ViewModel() {
    val notifications = notificationRepo.getAllNotifications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun markAllRead() { viewModelScope.launch { notificationRepo.markAllAsRead() } }
}

@Composable
fun NotificationsScreen(onBack: () -> Unit, viewModel: NotificationsViewModel = hiltViewModel()) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            THTopBar(title = "Notifications", onBack = onBack) {
                if (notifications.isNotEmpty()) {
                    IconButton(onClick = { viewModel.markAllRead() }) { Icon(Icons.Default.DoneAll, "Sab padha", tint = Color.White) }
                }
            }
        }
    ) { padding ->
        if (notifications.isEmpty()) {
            EmptyState(icon = Icons.Default.Notifications, title = "Koi notification nahi", modifier = Modifier.fillMaxSize().padding(padding))
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notifications) { notif ->
                    NotificationItem(notif)
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun NotificationItem(notif: AppNotification) {
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
