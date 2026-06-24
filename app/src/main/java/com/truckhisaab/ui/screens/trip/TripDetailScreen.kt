package com.truckhisaab.ui.screens.trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.TripStatus
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun TripDetailScreen(tripId: Long, onBack: () -> Unit, viewModel: TripViewModel = hiltViewModel()) {
    val trips by viewModel.trips.collectAsState()
    val trip = trips.find { it.id == tripId }
    val expenses by viewModel.getExpensesForTrip(tripId).collectAsState()
    var showDelete by remember { mutableStateOf(false) }

    if (trip == null) { LoadingState(); return }

    val totalExpense = expenses.sumOf { it.amount }
    val profit = trip.freightAmount - totalExpense

    Scaffold(topBar = {
        THTopBar("Trip Detail", onBack = onBack) {
            IconButton(onClick = { showDelete = true }) { Icon(Icons.Default.Delete, "Delete", tint = Color.White) }
        }
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                HeroCard(title = "${trip.fromLocation} → ${trip.toLocation}", value = formatINR(trip.freightAmount), subtitle = "Status: ${trip.status.hindiLabel}")
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard("Income", formatINR(trip.freightAmount), modifier = Modifier.weight(1f))
                    StatCard("Expense", formatINR(totalExpense), modifier = Modifier.weight(1f))
                    StatCard("Profit", formatINR(profit), change = if (profit >= 0) "+" else "", changePositive = profit >= 0, modifier = Modifier.weight(1f))
                }
            }
            item {
                THCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        DetailRow("Cargo", "${trip.cargoType} - ${trip.weightTons} Tons")
                        DetailRow("Party", trip.partyName)
                        DetailRow("Truck", trip.truckNumber)
                        DetailRow("Driver", trip.driverName)
                        DetailRow("Date", formatDate(trip.startDate))
                        if (trip.endDate != null) DetailRow("Completed", formatDate(trip.endDate))
                    }
                }
            }
            if (expenses.isNotEmpty()) {
                item { Text("Expenses (${expenses.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                items(expenses) { exp ->
                    THCard(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(12.dp)) {
                            Column(Modifier.weight(1f)) {
                                Text(exp.category.hindiLabel, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                if (exp.note.isNotBlank()) Text(exp.note, fontSize = 11.sp, color = TextSecondary)
                            }
                            Text(formatINR(exp.amount), fontWeight = FontWeight.Bold, color = DangerRed)
                        }
                    }
                }
            }
            if (trip.status == TripStatus.ACTIVE) {
                item {
                    THPrimaryButton("Trip Complete Karein", onClick = { viewModel.completeTrip(tripId) { onBack() } }, color = SuccessGreen, modifier = Modifier.fillMaxWidth())
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }

    if (showDelete) {
        ConfirmDialog("Trip Delete?", "Kya aap ye trip delete karna chahte hain?", onConfirm = { viewModel.deleteTrip(tripId) { onBack() } }, onDismiss = { showDelete = false })
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(label, Modifier.width(80.dp), color = TextSecondary, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}
