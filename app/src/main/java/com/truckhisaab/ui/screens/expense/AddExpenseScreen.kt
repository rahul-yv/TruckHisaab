package com.truckhisaab.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.truckhisaab.domain.model.ExpenseCategory
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun AddExpenseScreen(onBack: () -> Unit, viewModel: ExpenseViewModel = hiltViewModel()) {
    val state by viewModel.addState.collectAsState()
    val trucks by viewModel.trucks.collectAsState()

    val catIcons = mapOf(
        ExpenseCategory.DIESEL to Icons.Default.LocalGasStation, ExpenseCategory.TOLL to Icons.Default.Receipt,
        ExpenseCategory.TYRE to Icons.Default.TireRepair, ExpenseCategory.REPAIR to Icons.Default.Build,
        ExpenseCategory.FOOD to Icons.Default.Restaurant, ExpenseCategory.PARKING to Icons.Default.LocalParking,
        ExpenseCategory.OTHER to Icons.Default.MoreHoriz
    )

    Scaffold(topBar = { THTopBar("Kharcha Add", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text("Category", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            val rows = ExpenseCategory.entries.chunked(3)
            rows.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { cat ->
                        val selected = state.category == cat
                        Card(
                            Modifier.weight(1f).clickable { viewModel.updateAddState { copy(category = cat) } },
                            colors = CardDefaults.cardColors(containerColor = if (selected) Color(cat.colorHex).copy(0.15f) else Color.White),
                            border = if (selected) CardDefaults.outlinedCardBorder().copy(width = 2.dp) else null,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(catIcons[cat] ?: Icons.Default.MoreHoriz, null, tint = Color(cat.colorHex), modifier = Modifier.size(28.dp))
                                Spacer(Modifier.height(4.dp))
                                Text(cat.hindiLabel, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    if (row.size < 3) repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))
            THNumberField(state.amount, { viewModel.updateAddState { copy(amount = it) } }, "Amount (Raqam)")

            if (state.category == ExpenseCategory.DIESEL) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    THNumberField(state.liters, { viewModel.updateAddState { copy(liters = it) } }, "Litres", prefix = "", modifier = Modifier.weight(1f))
                    THNumberField(state.pricePerLiter, { viewModel.updateAddState { copy(pricePerLiter = it) } }, "Rate/L", modifier = Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("Truck Select Karein", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            trucks.forEach { truck ->
                FilterChip(
                    selected = state.truckId == truck.id,
                    onClick = { viewModel.updateAddState { copy(truckId = truck.id, truckNumber = truck.number) } },
                    label = { Text(truck.number) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White)
                )
            }

            Spacer(Modifier.height(12.dp))
            THTextField(state.note, { viewModel.updateAddState { copy(note = it) } }, "Note (Optional)", singleLine = false, maxLines = 3)

            Spacer(Modifier.height(24.dp))
            THPrimaryButton("Save Kharcha", onClick = { viewModel.saveExpense { onBack() } },
                loading = state.isSaving, enabled = state.category != null && state.amount.isNotBlank() && state.truckId > 0,
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
        }
    }
}
