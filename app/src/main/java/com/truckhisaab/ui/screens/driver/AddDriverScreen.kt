package com.truckhisaab.ui.screens.driver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.components.THTopBar

@Composable
fun AddDriverScreen(onBack: () -> Unit, onDone: () -> Unit, viewModel: DriverViewModel = hiltViewModel()) {
    val state by viewModel.addState.collectAsState()

    Scaffold(topBar = { THTopBar(title = "Naya Driver", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Driver Details", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(value = state.name, onValueChange = { v -> viewModel.updateAddState { copy(name = v) } }, label = { Text("Driver ka naam") }, leadingIcon = { Icon(Icons.Default.Person, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = state.phone, onValueChange = { v -> viewModel.updateAddState { copy(phone = v) } }, label = { Text("Phone number") }, leadingIcon = { Icon(Icons.Default.Phone, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            OutlinedTextField(value = state.license, onValueChange = { v -> viewModel.updateAddState { copy(license = v) } }, label = { Text("License Number") }, leadingIcon = { Icon(Icons.Default.Badge, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = state.salary, onValueChange = { v -> viewModel.updateAddState { copy(salary = v) } }, label = { Text("Monthly Salary (₹)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(Modifier.height(8.dp))
            THPrimaryButton(text = "Driver Save Karein", onClick = { viewModel.saveDriver { onDone() } }, modifier = Modifier.fillMaxWidth(), enabled = state.name.isNotBlank() && !state.isSaving, loading = state.isSaving)
            Spacer(Modifier.height(80.dp))
        }
    }
}
