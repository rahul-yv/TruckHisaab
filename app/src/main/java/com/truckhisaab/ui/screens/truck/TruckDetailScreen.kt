package com.truckhisaab.ui.screens.truck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun TruckDetailScreen(truckId: Long, onBack: () -> Unit, viewModel: TruckViewModel = hiltViewModel()) {
    val trucks by viewModel.trucks.collectAsState()
    val truck = trucks.find { it.id == truckId }
    val trips by viewModel.getTripsForTruck(truckId).collectAsState()
    val expenses by viewModel.getExpensesForTruck(truckId).collectAsState()
    val docs by viewModel.getDocsForTruck(truckId).collectAsState()

    if (truck == null) { LoadingState(); return }

    val totalIncome = trips.sumOf { it.freightAmount }
    val totalExpense = expenses.sumOf { it.amount }

    Scaffold(topBar = { THTopBar(truck.number, onBack = onBack) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                THCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Text("${truck.manufacturer} ${truck.model}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("${truck.type.label} • ${truck.yearOfPurchase}", fontSize = 13.sp, color = TextSecondary)
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard("Income", formatINR(totalIncome), modifier = Modifier.weight(1f))
                    StatCard("Expense", formatINR(totalExpense), modifier = Modifier.weight(1f))
                    StatCard("Profit", formatINR(totalIncome - totalExpense), modifier = Modifier.weight(1f))
                }
            }
            if (trips.isNotEmpty()) {
                item { Text("Trips (${trips.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                items(trips.take(5)) { trip ->
                    THCard(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(12.dp)) {
                            Text("${trip.fromLocation} → ${trip.toLocation}", Modifier.weight(1f), fontSize = 13.sp)
                            Text(formatINR(trip.freightAmount), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SuccessGreen)
                        }
                    }
                }
            }
            if (docs.isNotEmpty()) {
                item { Text("Documents (${docs.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                items(docs) { doc ->
                    THCard(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(12.dp)) {
                            val days = daysUntil(doc.expiryDate)
                            StatusDot(when { days < 0 -> DangerRed; days < 30 -> WarningOrange; else -> SuccessGreen }, 10)
                            Spacer(Modifier.width(10.dp))
                            Text("${doc.type.label} - ${doc.documentNumber}", Modifier.weight(1f), fontSize = 13.sp)
                            Text(if (days < 0) "Expired" else "$days din", fontSize = 12.sp, color = if (days < 0) DangerRed else SuccessGreen)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}
