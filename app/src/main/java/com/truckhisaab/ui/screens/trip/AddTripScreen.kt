package com.truckhisaab.ui.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.cargoTypes
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun AddTripScreen(onBack: () -> Unit, viewModel: TripViewModel = hiltViewModel()) {
    val state by viewModel.addState.collectAsState()
    val trucks by viewModel.trucks.collectAsState()
    val drivers by viewModel.drivers.collectAsState()
    val steps = listOf("Route", "Cargo", "Freight", "Driver", "Confirm")

    Scaffold(topBar = { THTopBar("Naya Trip", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                steps.forEachIndexed { i, label ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(28.dp).clip(CircleShape).background(if (i <= state.step) TruckRed else Color.LightGray), contentAlignment = Alignment.Center) {
                            Text("${i + 1}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(label, fontSize = 10.sp, color = if (i <= state.step) TruckRed else TextHint)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))

            Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                when (state.step) {
                    0 -> {
                        THTextField(state.from, { viewModel.updateAddState { copy(from = it) } }, "From (Kahaan se)", leadingIcon = Icons.Default.MyLocation)
                        Spacer(Modifier.height(12.dp))
                        THTextField(state.to, { viewModel.updateAddState { copy(to = it) } }, "To (Kahaan tak)", leadingIcon = Icons.Default.LocationOn)
                    }
                    1 -> {
                        Text("Cargo Type", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        LazyVerticalGrid(GridCells.Fixed(3), Modifier.height(280.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(cargoTypes) { cargo ->
                                FilterChip(selected = state.cargoType == cargo, onClick = { viewModel.updateAddState { copy(cargoType = cargo) } }, label = { Text(cargo, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        THNumberField(state.weight, { viewModel.updateAddState { copy(weight = it) } }, "Weight (Tons)", prefix = "")
                    }
                    2 -> {
                        THNumberField(state.freight, { viewModel.updateAddState { copy(freight = it) } }, "Freight Amount")
                        Spacer(Modifier.height(12.dp))
                        THNumberField(state.advance, { viewModel.updateAddState { copy(advance = it) } }, "Advance Amount")
                        Spacer(Modifier.height(12.dp))
                        THTextField(state.party, { viewModel.updateAddState { copy(party = it) } }, "Party Name", leadingIcon = Icons.Default.Person)
                    }
                    3 -> {
                        Text("Truck Select Karein", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        trucks.forEach { truck ->
                            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { viewModel.updateAddState { copy(selectedTruckId = truck.id, selectedTruckNumber = truck.number) } },
                                colors = CardDefaults.cardColors(containerColor = if (state.selectedTruckId == truck.id) TruckRedBg else Color.White), shape = RoundedCornerShape(8.dp)) {
                                Text("${truck.number} - ${truck.manufacturer} ${truck.model}", Modifier.padding(12.dp), fontWeight = if (state.selectedTruckId == truck.id) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Driver Select Karein", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        drivers.forEach { driver ->
                            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { viewModel.updateAddState { copy(selectedDriverId = driver.id, selectedDriverName = driver.name) } },
                                colors = CardDefaults.cardColors(containerColor = if (state.selectedDriverId == driver.id) TruckRedBg else Color.White), shape = RoundedCornerShape(8.dp)) {
                                Text("${driver.name} - ${driver.phone}", Modifier.padding(12.dp), fontWeight = if (state.selectedDriverId == driver.id) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                    4 -> {
                        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Trip Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                                SummaryRow("Route", "${state.from} → ${state.to}")
                                SummaryRow("Cargo", "${state.cargoType} - ${state.weight} Tons")
                                SummaryRow("Freight", "₹${state.freight}")
                                SummaryRow("Advance", "₹${state.advance}")
                                SummaryRow("Party", state.party)
                                SummaryRow("Truck", state.selectedTruckNumber)
                                SummaryRow("Driver", state.selectedDriverName)
                            }
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (state.step > 0) {
                    THSecondaryButton("Wapas", onClick = { viewModel.updateAddState { copy(step = step - 1) } }, modifier = Modifier.weight(1f))
                }
                THPrimaryButton(
                    text = if (state.step == 4) "Trip Save Karein" else "Aage",
                    onClick = {
                        if (state.step == 4) viewModel.saveTrip { onBack() }
                        else viewModel.updateAddState { copy(step = step + 1) }
                    },
                    loading = state.isSaving,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, Modifier.width(80.dp), color = TextSecondary, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}
