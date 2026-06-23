package com.truckhisaab.ui.screens.expense

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.ExpenseCategory
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun AddExpenseScreen(onBack: () -> Unit, onDone: () -> Unit, viewModel: ExpenseViewModel = viewModel()) {
    val state by viewModel.addState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.showSuccess) {
        if (state.showSuccess) {
            snackbar.showSnackbar("Kharcha save ho gaya!")
            viewModel.dismissSuccess()
            onDone()
        }
    }

    Scaffold(
        topBar = { THTopBar(title = "Naya Kharcha", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Category chuno", style = MaterialTheme.typography.headlineMedium)
            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(320.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ExpenseCategory.entries.toList()) { cat ->
                    Card(
                        Modifier.clickable { viewModel.setCategory(cat) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (state.category == cat) Color(cat.colorHex) else Color(cat.colorHex).copy(0.1f))
                    ) {
                        Column(Modifier.padding(12.dp, 10.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(cat.label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = if (state.category == cat) Color.White else Color(cat.colorHex), textAlign = TextAlign.Center)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.amount, onValueChange = { viewModel.updateField("amount", it) },
                label = { Text("Kitna kharcha? (₹)") }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.headlineLarge
            )

            OutlinedTextField(
                value = state.description, onValueChange = { viewModel.updateField("desc", it) },
                label = { Text("Kya kharcha hua? (Optional)") }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            if (state.category == ExpenseCategory.DIESEL) {
                Text("Fuel Details", fontWeight = FontWeight.SemiBold, color = TextSecondary)
                OutlinedTextField(value = state.liters, onValueChange = { viewModel.updateField("liters", it) }, label = { Text("Kitne litre?") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                OutlinedTextField(value = state.ratePerLiter, onValueChange = { viewModel.updateField("rate", it) }, label = { Text("Rate per litre (₹)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
            }

            val trucks = com.truckhisaab.data.AppContainer.truckRepository.trucks.value
            Text("Kaunsa truck?", fontSize = 14.sp, color = TextSecondary)
            androidx.compose.foundation.layout.Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                trucks.forEach { t ->
                    Card(Modifier.clickable { viewModel.updateField("truck", t.number) }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (state.truckNumber == t.number) TruckRed else Color(0xFFF5F5F5))) {
                        Text(t.number, Modifier.padding(10.dp, 6.dp), fontSize = 12.sp, color = if (state.truckNumber == t.number) Color.White else TextSecondary)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            THPrimaryButton(
                text = "💰 Kharcha Save Karein",
                onClick = { viewModel.saveExpense() },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.category != null && state.amount.isNotBlank()
            )
            Spacer(Modifier.height(80.dp))
        }
    }
}
