package com.truckhisaab.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.Trip
import com.truckhisaab.data.model.TripStatus
import com.truckhisaab.ui.components.EmptyState
import com.truckhisaab.ui.components.StatusBadge
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.components.formatTimeAgo
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.InfoBlue
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.SuccessGreenBg
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.TruckRedBg
import com.truckhisaab.ui.theme.WarningOrange
import com.truckhisaab.ui.theme.WarningOrangeBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToTrip: () -> Unit,
    onNavigateToExpense: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToDocuments: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToTripDetail: (String) -> Unit,
    onNavigateToActiveTrip: (String) -> Unit,
    onNavigateToToll: () -> Unit,
    onNavigateToRepair: () -> Unit,
    onNavigateToFuel: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(com.truckhisaab.ui.theme.Background)) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalShipping, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("TruckHisaab", fontWeight = FontWeight.Bold)
                }
            },
            actions = {
                IconButton(onClick = onNavigateToNotifications) {
                    BadgedBox(badge = {
                        if (state.unreadNotifications > 0)
                            Badge(containerColor = WarningOrange) { Text("${state.unreadNotifications}") }
                    }) {
                        Icon(Icons.Default.Notifications, "Notifications", tint = Color.White)
                    }
                }
                IconButton(onClick = onNavigateToSettings) {
                    Icon(Icons.Default.Settings, "Settings", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = TruckRed, titleContentColor = Color.White)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { HeroCard(state.todaySummary) }
            item { ContextCards(state, Modifier.padding(horizontal = 16.dp)) }
            item { RemindersWidget(state, Modifier.padding(horizontal = 16.dp), onNavigateToDocuments) }
            item {
                QuickActionsGrid(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onTrip = onNavigateToTrip, onFuel = onNavigateToFuel, onReport = onNavigateToReport,
                    onToll = onNavigateToToll, onRepair = onNavigateToRepair, onDoc = onNavigateToDocuments
                )
            }
            item { VoiceButton(Modifier.padding(horizontal = 16.dp)) }
            item { QuickStatsRow(state, Modifier.padding(horizontal = 16.dp)) }
            item { DieselWidget(state, Modifier.padding(horizontal = 16.dp)) }
            item {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Recent Trips", style = MaterialTheme.typography.headlineMedium)
                    Text("Sab dekhein >", color = TruckRed, fontSize = 13.sp, modifier = Modifier.clickable { })
                }
            }
            if (state.recentTrips.isEmpty()) {
                item { EmptyState(icon = Icons.Default.LocalShipping, title = "Koi trip nahi", actionLabel = "Naya Trip", onAction = onNavigateToTrip) }
            } else {
                items(state.recentTrips) { trip ->
                    TripListItem(trip, Modifier.padding(horizontal = 16.dp).clickable {
                        if (trip.status == TripStatus.ACTIVE) onNavigateToActiveTrip(trip.id) else onNavigateToTripDetail(trip.id)
                    })
                }
            }
            item { Spacer(Modifier.height(88.dp)) }
        }
    }
}

@Composable
private fun HeroCard(summary: PnLSummary) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TruckRed)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(summary.label, color = Color.White.copy(0.8f), fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(formatINR(summary.profit), color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            StatusBadge(
                label = if (summary.isProfit) "PROFIT" else "LOSS",
                color = if (summary.isProfit) SuccessGreen else Color.White
            )
            Spacer(Modifier.height(8.dp))
            Row {
                Text("Income: ${formatINR(summary.income)}", color = Color.White.copy(0.7f), fontSize = 12.sp)
                Spacer(Modifier.width(16.dp))
                Text("Expense: ${formatINR(summary.expense)}", color = Color.White.copy(0.7f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ContextCards(state: DashboardState, modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MiniPnlCard(state.weeklySummary, SuccessGreen)
        MiniPnlCard(state.monthlySummary, InfoBlue)
    }
}

@Composable
private fun MiniPnlCard(s: PnLSummary, accent: Color) {
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.width(180.dp)) {
        Column(Modifier.padding(14.dp)) {
            Text(s.label, fontSize = 12.sp, color = accent, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(formatINR(s.profit), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = if (s.isProfit) SuccessGreen else DangerRed)
            if (s.changePercent != 0.0) {
                Text("+${s.changePercent.toInt()}% vs last", fontSize = 11.sp, color = SuccessGreen)
            }
        }
    }
}

@Composable
private fun RemindersWidget(state: DashboardState, modifier: Modifier, onDocClick: () -> Unit) {
    if (state.expiringDocsCount == 0 && state.activeTripsCount == 0) return
    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = WarningOrangeBg)) {
        Column(Modifier.padding(14.dp)) {
            Text("Aaj ke kaam", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            if (state.activeTripsCount > 0) {
                Text("• ${state.activeTripsCount} trip chalu hai - kharcha track karo", fontSize = 13.sp, color = TextSecondary)
            }
            if (state.expiringDocsCount > 0) {
                Text("• ${state.expiringDocsCount} documents jaldi expire honge", fontSize = 13.sp, color = DangerRed, modifier = Modifier.clickable { onDocClick() })
            }
        }
    }
}

@Composable
private fun QuickActionsGrid(modifier: Modifier, onTrip: () -> Unit, onFuel: () -> Unit, onReport: () -> Unit, onToll: () -> Unit, onRepair: () -> Unit, onDoc: () -> Unit) {
    val items = listOf(
        Triple(Icons.Default.LocalShipping, "Trip", TruckRed) to onTrip,
        Triple(Icons.Default.LocalGasStation, "Fuel", Color.White) to onFuel,
        Triple(Icons.Default.Assessment, "Hisaab", Color.White) to onReport,
        Triple(Icons.Default.Toll, "Toll", Color.White) to onToll,
        Triple(Icons.Default.Build, "Repair", Color.White) to onRepair,
        Triple(Icons.Default.Description, "Doc", Color.White) to onDoc
    )
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (row in items.chunked(3)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { (item, onClick) ->
                    val (icon, label, bg) = item
                    val isRed = bg == TruckRed
                    Card(
                        modifier = Modifier.weight(1f).height(80.dp).clickable { onClick() },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isRed) TruckRed else Color.White),
                        elevation = CardDefaults.cardElevation(if (isRed) 4.dp else 1.dp)
                    ) {
                        Column(Modifier.fillMaxSize().padding(8.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(icon, label, tint = if (isRed) Color.White else TruckRed, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.height(4.dp))
                            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if (isRed) Color.White else TruckRed)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VoiceButton(modifier: Modifier) {
    Card(
        modifier = modifier.fillMaxWidth().clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = TruckRedBg)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Mic, null, tint = TruckRed, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Text("Bolke batao - Hindi mein", fontWeight = FontWeight.SemiBold, color = TruckRed, fontSize = 15.sp)
        }
    }
}

@Composable
private fun QuickStatsRow(state: DashboardState, modifier: Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        StatItem("${state.totalTripsThisMonth}", "Trips")
        StatItem("${state.totalKm}", "KM")
        StatItem(formatINR(state.monthlySummary.income), "Income")
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TruckRed)
        Text(label, fontSize = 11.sp, color = TextHint)
    }
}

@Composable
private fun DieselWidget(state: DashboardState, modifier: Modifier) {
    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocalGasStation, null, tint = WarningOrange, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Diesel Rate: ₹${state.dieselRate}/L", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(Modifier.weight(1f))
            Text("Kal se: +₹${state.dieselRateChange}", fontSize = 12.sp, color = DangerRed)
        }
    }
}

@Composable
fun TripListItem(trip: Trip, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(
                    when (trip.status) { TripStatus.ACTIVE -> SuccessGreenBg; TripStatus.COMPLETED -> InfoBlue.copy(0.1f); else -> Color(0xFFF5F5F5) }
                ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocalShipping, null, tint = when (trip.status) { TripStatus.ACTIVE -> SuccessGreen; TripStatus.COMPLETED -> InfoBlue; else -> TextHint }, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("${trip.fromLocation} → ${trip.toLocation}", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${trip.weightTons.toInt()}T ${trip.cargoType} | ${trip.partyName}", fontSize = 12.sp, color = TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(formatTimeAgo(trip.startDate), fontSize = 11.sp, color = TextHint)
            }
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(formatINR(trip.freightAmount), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                StatusBadge(trip.status.hindiLabel, when (trip.status) { TripStatus.ACTIVE -> SuccessGreen; TripStatus.COMPLETED -> InfoBlue; else -> TextHint })
            }
        }
    }
}
