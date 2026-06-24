package com.truckhisaab.ui.screens.documents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.DocumentType
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun AddDocumentScreen(onBack: () -> Unit, viewModel: DocumentsViewModel = hiltViewModel()) {
    val state by viewModel.addState.collectAsState()
    val trucks by viewModel.trucks.collectAsState()

    Scaffold(topBar = { THTopBar("Document Add", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text("Document Type", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            val rows = DocumentType.entries.chunked(2)
            rows.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { type ->
                        FilterChip(selected = state.type == type, onClick = { viewModel.updateAddState { copy(type = type) } }, label = { Text(type.label) },
                            modifier = Modifier.weight(1f), colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                    }
                    if (row.size < 2) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(4.dp))
            }

            Spacer(Modifier.height(16.dp))
            Text("Truck", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            trucks.forEach { truck ->
                Card(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { viewModel.updateAddState { copy(truckId = truck.id, truckNumber = truck.number) } },
                    colors = CardDefaults.cardColors(containerColor = if (state.truckId == truck.id) TruckRedBg else Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("${truck.number} - ${truck.manufacturer}", Modifier.padding(12.dp), fontWeight = if (state.truckId == truck.id) FontWeight.Bold else FontWeight.Normal) }
            }

            Spacer(Modifier.height(16.dp))
            THTextField(state.documentNumber, { viewModel.updateAddState { copy(documentNumber = it) } }, "Document Number")

            Spacer(Modifier.height(24.dp))
            THPrimaryButton("Save Document", onClick = { viewModel.saveDocument { onBack() } },
                loading = state.isSaving, enabled = state.type != null && state.truckId > 0 && state.documentNumber.isNotBlank(),
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
        }
    }
}
