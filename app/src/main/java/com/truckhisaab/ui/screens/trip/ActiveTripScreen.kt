package com.truckhisaab.ui.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.ui.components.ConfirmDialog
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.components.formatTimeAgo
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.WarningOrange

@Composable
fun ActiveTripScreen(
    tripId: String,
    onBack: () -> Unit,
    onAddExpense: () -> Unit,
    onComplete: () -> Unit,
    viewModel: TripViewModel = viewModel()
) {
    val trip = viewModel.getTrip(tripId) ?: run { onBack(); return }
    val expenses = viewModel.getTripExpenses(tripId)
    val totalExpense = expenses.sumOf { it.amount }
    var showComplete by remember { mutableStateOf(false) }

    if (showComplete) {
        ConfirmDialog(
            title = "Trip Khatam?",
            message = "Pakka? Final P&L calculate hoga.",
            confirmText = "Haan, khatam karo",
            onConfirm = { viewModel.completeTrip(tripId); onComplete() },
            onDismiss = { showComplete = false },
            confirmColor = SuccessGreen
        )
    }

    Scaffold(
        topBar = { THTopBar(title = "Trip Chalu", onBack = onBack, containerColor = SuccessGreen) }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(0.1f))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("🟢 Trip Chalu Hai", fontWeight = FontWeight.Bold, color = SuccessGreen, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("${trip.fromLocation} → ${trip.toLocation}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("${trip.weightTons.toInt()} Ton ${trip.cargoType}", fontSize = 14.sp, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Shuru hua", fontSize = 12.sp, color = TextSecondary)
                            Text(formatTimeAgo(trip.startDate), fontWeight = FontWeight.SemiBold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Freight", fontSize = 12.sp, color = TextSecondary)
                            Text(formatINR(trip.freightAmount), fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                    }
                }
            }

            Text("Quick Expense", Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickExpenseBtn(Icons.Default.LocalGasStation, "Fuel", TruckRed, Modifier.weight(1f), onAddExpense)
                QuickExpenseBtn(Icons.Default.Toll, "Toll", WarningOrange, Modifier.weight(1f), onAddExpense)
                QuickExpenseBtn(Icons.Default.Build, "Repair", Color(0xFF9C27B0), Modifier.weight(1f), onAddExpense)
                QuickExpenseBtn(Icons.Default.Restaurant, "Khaana", SuccessGreen, Modifier.weight(1f), onAddExpense)
            }

            if (expenses.isNotEmpty()) {
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Trip Expenses", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        expenses.forEach { e ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${e.category.label}", fontSize = 13.sp)
                                Text(formatINR(e.amount), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold)
                            Text(formatINR(totalExpense), fontWeight = FontWeight.Bold, color = DangerRed)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            THPrimaryButton(
                text = "Trip Khatam Karein",
                onClick = { showComplete = true },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                color = DangerRed
            )
            Spacer(Modifier.height(88.dp))
        }
    }
}

@Composable
private fun QuickExpenseBtn(icon: ImageVector, label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(modifier = modifier.height(72.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f)), onClick = onClick) {
        Column(Modifier.fillMaxSize().padding(8.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, label, tint = color, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(4.dp))
            Text(label, fontSize = 11.sp, color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}
