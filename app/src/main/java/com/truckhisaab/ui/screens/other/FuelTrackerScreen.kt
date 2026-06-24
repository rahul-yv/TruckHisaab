package com.truckhisaab.ui.screens.other

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.screens.expense.ExpenseViewModel
import com.truckhisaab.ui.theme.*

@Composable
fun FuelTrackerScreen(onBack: () -> Unit, viewModel: ExpenseViewModel = hiltViewModel()) {
    val entries by viewModel.fuelEntries.collectAsState()
    val totalLiters = entries.sumOf { it.liters }
    val totalAmount = entries.sumOf { it.totalAmount }
    val avgRate = if (entries.isNotEmpty()) entries.sumOf { it.pricePerLiter } / entries.size else 0.0

    Scaffold(topBar = { THTopBar(title = "Fuel Tracker", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard("Total Litres", "%.0f L".format(totalLiters), TruckRed, Modifier.weight(1f))
                SummaryCard("Total Cost", formatINR(totalAmount), DangerRed, Modifier.weight(1f))
                SummaryCard("Avg Rate", "₹%.1f/L".format(avgRate), WarningOrange, Modifier.weight(1f))
            }

            if (entries.isEmpty()) {
                EmptyState(icon = Icons.Default.LocalGasStation, title = "Koi fuel entry nahi", subtitle = "Expense mein fuel add karein")
            } else {
                LazyColumn(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(entries.sortedByDescending { it.date }) { entry ->
                        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
                            Row(Modifier.padding(14.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("${entry.liters.toInt()} Litres @ ₹${entry.pricePerLiter}/L", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text("Truck: ${entry.truckNumber}", fontSize = 12.sp, color = TextSecondary)
                                    Text(formatDate(entry.date), fontSize = 11.sp, color = TextHint)
                                }
                                Text(formatINR(entry.totalAmount), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DangerRed)
                            }
                        }
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, color: Color, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
        Column(Modifier.padding(10.dp)) {
            Text(label, fontSize = 10.sp, color = TextSecondary)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
        }
    }
}
