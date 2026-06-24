package com.truckhisaab.ui.screens.report

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
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun ReportScreen(onBack: () -> Unit, viewModel: ReportViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val profit = state.totalIncome - state.totalExpense
    val periods = listOf("today" to "Aaj", "week" to "Hafta", "month" to "Mahina", "year" to "Saal")

    Scaffold(topBar = { THTopBar("P&L Report", onBack = onBack) }) { padding ->
        if (state.isLoading) { LoadingState(); return@Scaffold }
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    periods.forEach { (key, label) ->
                        FilterChip(selected = state.period == key, onClick = { viewModel.loadReport(key) }, label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                    }
                }
            }
            item {
                HeroCard("Net Profit / Loss", formatINR(profit), subtitle = "Income: ${formatINR(state.totalIncome)} | Expense: ${formatINR(state.totalExpense)}")
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard("Income", formatINR(state.totalIncome), modifier = Modifier.weight(1f))
                    StatCard("Expense", formatINR(state.totalExpense), modifier = Modifier.weight(1f))
                }
            }

            if (state.categoryBreakdown.isNotEmpty()) {
                item { Text("Category Breakdown", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                items(state.categoryBreakdown.sortedByDescending { it.total }) { cat ->
                    THCard(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            StatusDot(Color(cat.category.colorHex), 10)
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(cat.category.hindiLabel, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("${cat.percentage.toInt()}%", fontSize = 11.sp, color = TextSecondary)
                            }
                            Text(formatINR(cat.total), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            if (state.truckBreakdown.isNotEmpty()) {
                item { Text("Truck-wise P&L", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                items(state.truckBreakdown.sortedByDescending { it.profit }) { truck ->
                    THCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Row(Modifier.fillMaxWidth()) {
                                Text(truck.truckNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                                Text(formatINR(truck.profit), fontWeight = FontWeight.Bold, color = if (truck.profit >= 0) SuccessGreen else DangerRed)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Income: ${formatINR(truck.income)} | Expense: ${formatINR(truck.expense)} | ${truck.trips} trips", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }

            if (state.partyBreakdown.isNotEmpty()) {
                item { Text("Party-wise", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                items(state.partyBreakdown.take(10)) { party ->
                    THCard(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(12.dp)) {
                            Column(Modifier.weight(1f)) {
                                Text(party.partyName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("${party.trips} trips", fontSize = 11.sp, color = TextSecondary)
                            }
                            Text(formatINR(party.totalAmount), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}
