package com.truckhisaab.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.*
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigateToTrip: () -> Unit,
    onNavigateToExpense: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToDocuments: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToTripDetail: (Long) -> Unit,
    onNavigateToActiveTrip: (Long) -> Unit,
    onNavigateToToll: () -> Unit,
    onNavigateToRepair: () -> Unit,
    onNavigateToFuel: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            THTopBar(title = "TruckHisaab") {
                BadgedBox(badge = { if (state.unreadNotifications > 0) Badge { Text("${state.unreadNotifications}") } }) {
                    IconButton(onClick = onNavigateToNotifications) { Icon(Icons.Default.Notifications, "Notifications", tint = Color.White) }
                }
            }
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Spacer(Modifier.height(4.dp)) }

            item {
                HeroCard(
                    title = "Aaj ka Hisaab",
                    value = formatINR(state.todayIncome - state.todayExpense),
                    subtitle = "Income: ${formatINR(state.todayIncome)} | Expense: ${formatINR(state.todayExpense)}"
                )
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Is Hafte", formatINR(state.weekIncome - state.weekExpense), modifier = Modifier.weight(1f))
                    StatCard("Is Mahine", formatINR(state.monthIncome - state.monthExpense), modifier = Modifier.weight(1f))
                }
            }

            item {
                Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                val actions = listOf(
                    Triple(Icons.Default.LocalShipping, "Trip") { onNavigateToTrip() },
                    Triple(Icons.Default.LocalGasStation, "Diesel") { onNavigateToFuel() },
                    Triple(Icons.Default.Assessment, "P&L") { onNavigateToReport() },
                    Triple(Icons.Default.Receipt, "Toll") { onNavigateToToll() },
                    Triple(Icons.Default.Build, "Repair") { onNavigateToRepair() },
                    Triple(Icons.Default.Description, "Documents") { onNavigateToDocuments() }
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (row in actions.chunked(3)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for ((icon, label, action) in row) {
                                QuickActionItem(icon = icon, label = label, onClick = action, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoChip(Icons.Default.LocalShipping, "${state.truckCount} Trucks", Modifier.weight(1f))
                    InfoChip(Icons.Default.Person, "${state.driverCount} Drivers", Modifier.weight(1f))
                    InfoChip(Icons.Default.Route, "${state.tripCount} Trips", Modifier.weight(1f))
                }
            }

            if (state.expiringDocs.isNotEmpty()) {
                item {
                    Text("Document Alerts", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DangerRed)
                    Spacer(Modifier.height(4.dp))
                    state.expiringDocs.take(3).forEach { doc ->
                        val days = daysUntil(doc.expiryDate)
                        Card(Modifier.fillMaxWidth().padding(vertical = 2.dp), colors = CardDefaults.cardColors(containerColor = if (days < 0) DangerRedBg else WarningOrangeBg)) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                StatusDot(if (days < 0) DangerRed else WarningOrange, 10)
                                Spacer(Modifier.width(8.dp))
                                Column(Modifier.weight(1f)) {
                                    Text("${doc.type.label} - ${doc.truckNumber}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text(if (days < 0) "${-days} din pehle expire" else "$days din baaki", fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            if (state.recentTrips.isNotEmpty()) {
                item { Text("Recent Trips", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                items(state.recentTrips) { trip ->
                    TripListItem(trip = trip, onClick = {
                        if (trip.status == TripStatus.ACTIVE) onNavigateToActiveTrip(trip.id)
                        else onNavigateToTripDetail(trip.id)
                    })
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun QuickActionItem(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier) {
    Card(modifier.clickable(onClick = onClick), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(Modifier.padding(12.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(40.dp).clip(CircleShape).background(TruckRedBg), contentAlignment = Alignment.Center) {
                Icon(icon, null, Modifier.size(22.dp), tint = TruckRed)
            }
            Spacer(Modifier.height(6.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, label: String, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, Modifier.size(16.dp), tint = TruckRed)
            Spacer(Modifier.width(4.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun TripListItem(trip: Trip, onClick: () -> Unit) {
    val statusColor = when (trip.status) {
        TripStatus.ACTIVE -> SuccessGreen; TripStatus.COMPLETED -> InfoBlue
        TripStatus.CANCELLED -> DangerRed; else -> TextSecondary
    }
    THCard(Modifier.fillMaxWidth(), onClick = onClick) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            StatusDot(statusColor, 10)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("${trip.fromLocation} → ${trip.toLocation}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("${trip.cargoType} • ${trip.truckNumber}", fontSize = 12.sp, color = TextSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(formatINR(trip.freightAmount), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TruckRed)
                Text(formatTimeAgo(trip.startDate), fontSize = 11.sp, color = TextHint)
            }
        }
    }
}
