package com.truckhisaab.ui.screens.truck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.TruckType
import com.truckhisaab.domain.model.truckManufacturers
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun AddTruckScreen(onBack: () -> Unit, viewModel: TruckViewModel = hiltViewModel()) {
    val state by viewModel.addState.collectAsState()

    Scaffold(topBar = { THTopBar("Truck Add", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            THTextField(state.number, { viewModel.updateAddState { copy(number = it) } }, "Truck Number (e.g. MH12AB1234)")

            Spacer(Modifier.height(16.dp))
            Text("Type", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TruckType.entries.forEach { type ->
                    FilterChip(selected = state.type == type, onClick = { viewModel.updateAddState { copy(type = type) } },
                        label = { Text(type.label) }, modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Manufacturer", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            val mfgRows = truckManufacturers.chunked(3)
            mfgRows.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { mfg ->
                        FilterChip(selected = state.manufacturer == mfg, onClick = { viewModel.updateAddState { copy(manufacturer = mfg) } },
                            label = { Text(mfg) }, modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                    }
                    if (row.size < 3) repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(4.dp))
            }

            Spacer(Modifier.height(16.dp))
            THTextField(state.model, { viewModel.updateAddState { copy(model = it) } }, "Model")
            Spacer(Modifier.height(12.dp))
            THTextField(state.year, { viewModel.updateAddState { copy(year = it) } }, "Year of Purchase")

            Spacer(Modifier.height(24.dp))
            THPrimaryButton("Save Truck", onClick = { viewModel.saveTruck { onBack() } },
                loading = state.isSaving, enabled = state.number.isNotBlank() && state.manufacturer.isNotBlank(),
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
        }
    }
}
