package com.truckhisaab.ui.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.cargoTypes
import com.truckhisaab.data.model.popularRoutes
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(onBack: () -> Unit, onDone: () -> Unit, viewModel: TripViewModel = viewModel()) {
    val state by viewModel.addState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.showSuccess) {
        if (state.showSuccess) {
            snackbar.showSnackbar("Trip save ho gaya!")
            viewModel.dismissSuccess()
            onDone()
        }
    }

    Scaffold(
        topBar = {
            THTopBar(title = "Naya Trip", onBack = {
                if (state.step > 1) viewModel.prevStep() else onBack()
            }, actions = {
                TextButton(onClick = onBack) { Text("Cancel", color = Color.White) }
            })
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LinearProgressIndicator(
                progress = { state.step / 5f },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = TruckRed
            )

            StepIndicator(state.step)

            Column(
                Modifier.weight(1f).padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (state.step) {
                    1 -> RouteStep(state, viewModel)
                    2 -> CargoStep(state, viewModel)
                    3 -> FreightStep(state, viewModel)
                    4 -> DriverTruckStep(state, viewModel)
                    5 -> ConfirmStep(state, viewModel)
                }
            }

            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.step > 1) {
                    com.truckhisaab.ui.components.THSecondaryButton(
                        text = "◀ Wapas",
                        onClick = { viewModel.prevStep() },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (state.step < 5) {
                    THPrimaryButton(
                        text = "Aage ▶",
                        onClick = { viewModel.nextStep() },
                        modifier = Modifier.weight(1f),
                        enabled = viewModel.canProceed()
                    )
                } else {
                    THPrimaryButton(
                        text = "✅ Trip Shuru Karein",
                        onClick = { viewModel.saveTrip() },
                        modifier = Modifier.weight(1f),
                        color = SuccessGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(current: Int) {
    Row(Modifier.fillMaxWidth().padding(16.dp, 12.dp), horizontalArrangement = Arrangement.Center) {
        val labels = listOf("Route", "Cargo", "Freight", "Driver", "Confirm")
        labels.forEachIndexed { i, label ->
            val step = i + 1
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(28.dp).clip(CircleShape).background(
                        when { step < current -> SuccessGreen; step == current -> TruckRed; else -> Color.LightGray }
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    if (step < current) Icon(Icons.Default.Check, null, Modifier.size(16.dp), tint = Color.White)
                    else Text("$step", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Text(label, fontSize = 10.sp, color = if (step == current) TruckRed else TextHint)
            }
            if (i < labels.lastIndex) {
                Box(Modifier.padding(top = 14.dp).width(24.dp).height(2.dp).background(if (step < current) SuccessGreen else Color.LightGray))
            }
        }
    }
}

@Composable
private fun RouteStep(state: AddTripState, vm: TripViewModel) {
    Text("Kahan se? Kahan tak?", style = MaterialTheme.typography.headlineMedium)
    OutlinedTextField(value = state.from, onValueChange = { vm.updateAddField("from", it) }, label = { Text("Kahan se?") }, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
    OutlinedTextField(value = state.to, onValueChange = { vm.updateAddField("to", it) }, label = { Text("Kahan tak?") }, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
    Text("Popular Routes", fontSize = 13.sp, color = TextSecondary)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        popularRoutes.take(3).forEach { (f, t) ->
            Card(Modifier.clickable { vm.updateAddField("from", f); vm.updateAddField("to", t) }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = TruckRed.copy(0.08f))) {
                Text("$f→$t", Modifier.padding(8.dp, 4.dp), fontSize = 12.sp, color = TruckRed)
            }
        }
    }
}

@Composable
private fun CargoStep(state: AddTripState, vm: TripViewModel) {
    Text("Kya load hai?", style = MaterialTheme.typography.headlineMedium)
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(280.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(cargoTypes.take(12)) { type ->
            Card(
                Modifier.clickable { vm.updateAddField("cargo", type) },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = if (state.cargoType == type) TruckRed else Color(0xFFF5F5F5))
            ) {
                Text(type, Modifier.padding(12.dp, 10.dp).fillMaxWidth(), fontSize = 13.sp, fontWeight = if (state.cargoType == type) FontWeight.Bold else FontWeight.Normal, color = if (state.cargoType == type) Color.White else TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    }
    OutlinedTextField(value = state.weight, onValueChange = { vm.updateAddField("weight", it) }, label = { Text("Kitna weight? (Tons)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
}

@Composable
private fun FreightStep(state: AddTripState, vm: TripViewModel) {
    Text("Kitna freight?", style = MaterialTheme.typography.headlineMedium)
    OutlinedTextField(value = state.freight, onValueChange = { vm.updateAddField("freight", it) }, label = { Text("Freight Amount (₹)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), textStyle = MaterialTheme.typography.headlineLarge)
    OutlinedTextField(value = state.partyName, onValueChange = { vm.updateAddField("party", it) }, label = { Text("Party ka naam?") }, leadingIcon = { Icon(Icons.Default.Person, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
    OutlinedTextField(value = state.advance, onValueChange = { vm.updateAddField("advance", it) }, label = { Text("Advance mila? (₹) - Optional") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
}

@Composable
private fun DriverTruckStep(state: AddTripState, vm: TripViewModel) {
    Text("Driver aur Truck", style = MaterialTheme.typography.headlineMedium)
    val drivers = com.truckhisaab.data.AppContainer.driverRepository.drivers.value
    val trucks = com.truckhisaab.data.AppContainer.truckRepository.trucks.value

    Text("Driver kaun?", fontSize = 14.sp, color = TextSecondary)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        drivers.take(3).forEach { d ->
            Card(Modifier.clickable { vm.updateAddField("driver", d.name) }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (state.driverName == d.name) TruckRed else Color(0xFFF5F5F5))) {
                Text(d.name, Modifier.padding(10.dp, 6.dp), fontSize = 13.sp, color = if (state.driverName == d.name) Color.White else TextSecondary)
            }
        }
    }
    OutlinedTextField(value = state.driverName, onValueChange = { vm.updateAddField("driver", it) }, label = { Text("Ya naam likho") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

    Spacer(Modifier.height(8.dp))
    Text("Kaunsa truck?", fontSize = 14.sp, color = TextSecondary)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        trucks.forEach { t ->
            Card(Modifier.clickable { vm.updateAddField("truck", t.number) }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (state.truckNumber == t.number) TruckRed else Color(0xFFF5F5F5))) {
                Text(t.number, Modifier.padding(10.dp, 6.dp), fontSize = 12.sp, color = if (state.truckNumber == t.number) Color.White else TextSecondary)
            }
        }
    }
}

@Composable
private fun ConfirmStep(state: AddTripState, vm: TripViewModel) {
    Text("Trip Summary", style = MaterialTheme.typography.headlineMedium)
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SummaryRow("Route", "${state.from} → ${state.to}")
            SummaryRow("Cargo", "${state.cargoType} • ${state.weight} Ton")
            SummaryRow("Freight", "₹${state.freight}")
            if (state.advance.isNotBlank()) SummaryRow("Advance", "₹${state.advance}")
            SummaryRow("Party", state.partyName)
            SummaryRow("Driver", state.driverName)
            SummaryRow("Truck", state.truckNumber)
        }
    }
    TextButton(onClick = { vm.prevStep(); vm.prevStep(); vm.prevStep(); vm.prevStep() }) {
        Text("✏️ Edit karein", color = TruckRed)
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
