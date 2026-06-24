package com.truckhisaab.ui.screens.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.ExpenseCategory
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun ActiveTripScreen(tripId: Long, onBack: () -> Unit, onAddExpense: () -> Unit, viewModel: TripViewModel = hiltViewModel()) {
    val trips by viewModel.trips.collectAsState()
    val trip = trips.find { it.id == tripId }
    val expenses by viewModel.getExpensesForTrip(tripId).collectAsState()
    var showComplete by remember { mutableStateOf(false) }

    if (trip == null) { LoadingState(); return }

    Scaffold(topBar = { THTopBar("Live Trip", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            HeroCard(title = "${trip.fromLocation} → ${trip.toLocation}", value = formatINR(trip.freightAmount), subtitle = "${trip.cargoType} • ${trip.truckNumber}")
            Spacer(Modifier.height(16.dp))

            Text("Quick Kharcha Add", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ExpenseCategory.entries.take(4).forEach { cat ->
                    THSecondaryButton(cat.hindiLabel, onClick = onAddExpense, modifier = Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Total Kharcha: ${formatINR(expenses.sumOf { it.amount })}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DangerRed)
            Spacer(Modifier.weight(1f))

            THPrimaryButton("Trip Complete Karein", onClick = { showComplete = true }, color = SuccessGreen, modifier = Modifier.fillMaxWidth())
        }
    }

    if (showComplete) {
        ConfirmDialog("Trip Complete?", "Kya trip poora ho gaya?", confirmText = "Haan, Complete", onConfirm = { viewModel.completeTrip(tripId) { onBack() } }, onDismiss = { showComplete = false }, confirmColor = SuccessGreen)
    }
}
