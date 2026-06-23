package com.truckhisaab.ui.screens.driver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.components.THTopBar

@Composable
fun AddDriverScreen(onBack: () -> Unit, onDone: () -> Unit, viewModel: DriverViewModel = viewModel()) {
    val state by viewModel.addState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.showSuccess) {
        if (state.showSuccess) { snackbar.showSnackbar("Driver add ho gaya!"); viewModel.dismissSuccess(); onDone() }
    }

    Scaffold(topBar = { THTopBar(title = "Naya Driver", onBack = onBack) }, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Driver Details", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(value = state.name, onValueChange = { viewModel.updateField("name", it) }, label = { Text("Driver ka naam") }, leadingIcon = { Icon(Icons.Default.Person, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = state.phone, onValueChange = { viewModel.updateField("phone", it) }, label = { Text("Phone number") }, leadingIcon = { Icon(Icons.Default.Phone, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            OutlinedTextField(value = state.licenseNumber, onValueChange = { viewModel.updateField("license", it) }, label = { Text("License Number") }, leadingIcon = { Icon(Icons.Default.Badge, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = state.salary, onValueChange = { viewModel.updateField("salary", it) }, label = { Text("Monthly Salary (₹)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(Modifier.height(8.dp))
            THPrimaryButton(text = "👨‍✈️ Driver Save Karein", onClick = { viewModel.saveDriver() }, modifier = Modifier.fillMaxWidth(), enabled = state.name.isNotBlank())
            Spacer(Modifier.height(80.dp))
        }
    }
}
