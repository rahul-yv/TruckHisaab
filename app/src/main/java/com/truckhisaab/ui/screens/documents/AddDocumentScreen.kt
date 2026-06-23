package com.truckhisaab.ui.screens.documents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.DocumentType
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun AddDocumentScreen(onBack: () -> Unit, onDone: () -> Unit, viewModel: DocumentsViewModel = viewModel()) {
    val state by viewModel.addState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.showSuccess) {
        if (state.showSuccess) { snackbar.showSnackbar("Document save ho gaya!"); viewModel.dismissSuccess(); onDone() }
    }

    Scaffold(topBar = { THTopBar(title = "Naya Document", onBack = onBack) }, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Document Type", style = MaterialTheme.typography.headlineMedium)
            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(280.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(DocumentType.entries.toList()) { type ->
                    Card(Modifier.clickable { viewModel.setDocType(type) }, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = if (state.type == type) TruckRed else Color(0xFFF5F5F5))) {
                        Text(type.label, Modifier.padding(14.dp, 12.dp).fillMaxWidth(), fontSize = 13.sp, fontWeight = if (state.type == type) FontWeight.Bold else FontWeight.Normal, color = if (state.type == type) Color.White else TextSecondary, textAlign = TextAlign.Center)
                    }
                }
            }

            OutlinedTextField(value = state.number, onValueChange = { viewModel.updateAddField("number", it) }, label = { Text("Document Number") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

            val trucks = com.truckhisaab.data.AppContainer.truckRepository.trucks.value
            Text("Kaunsa truck?", fontSize = 14.sp, color = TextSecondary)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                trucks.forEach { t ->
                    Card(Modifier.clickable { viewModel.updateAddField("truck", t.number) }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (state.truckNumber == t.number) TruckRed else Color(0xFFF5F5F5))) {
                        Text(t.number, Modifier.padding(10.dp, 6.dp), fontSize = 12.sp, color = if (state.truckNumber == t.number) Color.White else TextSecondary)
                    }
                }
            }

            Text("Reminder: ${state.reminderDays} din pehle", fontSize = 14.sp, color = TextSecondary)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 60, 90).forEach { d ->
                    Card(Modifier.clickable { viewModel.setReminderDays(d) }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (state.reminderDays == d) TruckRed else Color(0xFFF5F5F5))) {
                        Text("$d din", Modifier.padding(10.dp, 6.dp), fontSize = 12.sp, color = if (state.reminderDays == d) Color.White else TextSecondary)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            THPrimaryButton(text = "📄 Document Save Karein", onClick = { viewModel.saveDoc() }, modifier = Modifier.fillMaxWidth(), enabled = state.type != null && state.number.isNotBlank())
            Spacer(Modifier.height(80.dp))
        }
    }
}
