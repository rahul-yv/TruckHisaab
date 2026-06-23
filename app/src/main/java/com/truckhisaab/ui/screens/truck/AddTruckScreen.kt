package com.truckhisaab.ui.screens.truck

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.TruckType
import com.truckhisaab.data.model.truckManufacturers
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun AddTruckScreen(onBack: () -> Unit, onDone: () -> Unit, viewModel: TruckViewModel = viewModel()) {
    val state by viewModel.addState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.showSuccess) {
        if (state.showSuccess) { snackbar.showSnackbar("Truck add ho gaya!"); viewModel.dismissSuccess(); onDone() }
    }

    Scaffold(topBar = { THTopBar(title = "Naya Truck", onBack = onBack) }, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Truck Details", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(value = state.number, onValueChange = { viewModel.updateField("number", it.uppercase()) }, label = { Text("Truck Number (e.g. MH04AB1234)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

            Text("Truck Type", fontSize = 14.sp, color = TextSecondary)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TruckType.entries.forEach { t ->
                    Card(Modifier.weight(1f).clickable { viewModel.setType(t) }, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = if (state.type == t) TruckRed else Color(0xFFF5F5F5))) {
                        Text(t.label, Modifier.padding(12.dp, 10.dp).fillMaxWidth(), fontSize = 12.sp, fontWeight = if (state.type == t) FontWeight.Bold else FontWeight.Normal, color = if (state.type == t) Color.White else TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }

            Text("Manufacturer", fontSize = 14.sp, color = TextSecondary)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                truckManufacturers.take(4).forEach { m ->
                    Card(Modifier.weight(1f).clickable { viewModel.updateField("manufacturer", m) }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (state.manufacturer == m) TruckRed else Color(0xFFF5F5F5))) {
                        Text(m, Modifier.padding(8.dp, 6.dp).fillMaxWidth(), fontSize = 11.sp, color = if (state.manufacturer == m) Color.White else TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }

            OutlinedTextField(value = state.model, onValueChange = { viewModel.updateField("model", it) }, label = { Text("Model Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = state.year, onValueChange = { viewModel.updateField("year", it) }, label = { Text("Year") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

            Spacer(Modifier.height(8.dp))
            THPrimaryButton(text = "🚛 Truck Save Karein", onClick = { viewModel.saveTruck() }, modifier = Modifier.fillMaxWidth(), enabled = state.number.isNotBlank() && state.type != null)
            Spacer(Modifier.height(80.dp))
        }
    }
}
