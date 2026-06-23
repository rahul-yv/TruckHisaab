package com.truckhisaab.ui.screens.trip

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.ui.components.ConfirmDialog
import com.truckhisaab.ui.components.StatusBadge
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatDate
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.InfoBlue
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.SuccessGreenBg
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.WhatsAppGreen

@Composable
fun TripDetailScreen(tripId: String, onBack: () -> Unit, viewModel: TripViewModel = viewModel()) {
    val trip = viewModel.getTrip(tripId) ?: run { onBack(); return }
    val expenses = viewModel.getTripExpenses(tripId)
    val totalExpense = expenses.sumOf { it.amount }
    val profit = trip.freightAmount - totalExpense
    var showDelete by remember { mutableStateOf(false) }

    if (showDelete) {
        ConfirmDialog("Delete Trip", "Pakka delete karna hai? Ye wapas nahi aayega.", onConfirm = { viewModel.deleteTrip(tripId); onBack() }, onDismiss = { showDelete = false })
    }

    Scaffold(
        topBar = {
            THTopBar(title = "${trip.fromLocation} → ${trip.toLocation}", onBack = onBack, containerColor = InfoBlue, actions = {
                IconButton(onClick = { showDelete = true }) { Icon(Icons.Default.Delete, "Delete", tint = Color.White) }
            })
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Trip Details", fontWeight = FontWeight.Bold)
                        StatusBadge(trip.status.hindiLabel, InfoBlue)
                    }
                    HorizontalDivider()
                    DetailRow("Route", "${trip.fromLocation} → ${trip.toLocation}")
                    DetailRow("Cargo", "${trip.cargoType} • ${trip.weightTons.toInt()} Ton")
                    DetailRow("Freight", formatINR(trip.freightAmount))
                    if (trip.advanceAmount > 0) DetailRow("Advance", formatINR(trip.advanceAmount))
                    DetailRow("Party", trip.partyName)
                    DetailRow("Driver", trip.driverName)
                    DetailRow("Truck", trip.truckNumber)
                    DetailRow("Started", formatDate(trip.startDate, "dd MMM yyyy, hh:mm a"))
                    if (trip.endDate != null) DetailRow("Ended", formatDate(trip.endDate, "dd MMM yyyy, hh:mm a"))
                }
            }

            if (expenses.isNotEmpty()) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Trip Expenses", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        expenses.forEach { exp ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Row {
                                    Box(Modifier.size(8.dp).clip(CircleShape).background(Color(exp.category.colorHex)).align(Alignment.CenterVertically))
                                    Spacer(Modifier.width(8.dp))
                                    Text(exp.category.label, fontSize = 14.sp)
                                }
                                Text(formatINR(exp.amount), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Expense", fontWeight = FontWeight.Bold)
                            Text(formatINR(totalExpense), fontWeight = FontWeight.Bold, color = DangerRed)
                        }
                    }
                }
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = if (profit >= 0) SuccessGreenBg else DangerRed.copy(0.1f))) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Trip P&L", fontSize = 14.sp, color = TextSecondary)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Income", fontSize = 12.sp, color = TextHint); Text(formatINR(trip.freightAmount), fontWeight = FontWeight.Bold, color = SuccessGreen) }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Expense", fontSize = 12.sp, color = TextHint); Text(formatINR(totalExpense), fontWeight = FontWeight.Bold, color = DangerRed) }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Profit", fontSize = 12.sp, color = TextHint); Text(formatINR(profit), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (profit >= 0) SuccessGreen else DangerRed) }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                THPrimaryButton("PDF Download", { }, Modifier.weight(1f), color = TruckRed)
                THPrimaryButton("WhatsApp", { }, Modifier.weight(1f), color = WhatsAppGreen)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
