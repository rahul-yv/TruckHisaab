package com.truckhisaab.ui.screens.documents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.getDocumentStatus
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun DocumentListScreen(onBack: () -> Unit, onAddDoc: () -> Unit, onDocDetail: (Long) -> Unit, viewModel: DocumentsViewModel = hiltViewModel()) {
    val docs by viewModel.filteredDocs.collectAsState()
    val trucks by viewModel.trucks.collectAsState()
    val truckFilter by viewModel.truckFilter.collectAsState()

    Scaffold(
        topBar = { THTopBar("Documents", onBack = onBack) },
        floatingActionButton = { QuickAddFab(onClick = onAddDoc) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyRow(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(selected = truckFilter == 0L, onClick = { viewModel.setTruckFilter(0) }, label = { Text("Sab") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White)) }
                items(trucks) { truck ->
                    FilterChip(selected = truckFilter == truck.id, onClick = { viewModel.setTruckFilter(truck.id) }, label = { Text(truck.number) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                }
            }
            if (docs.isEmpty()) {
                EmptyState(icon = Icons.Default.Description, title = "Koi document nahi", actionLabel = "Document Add", onAction = onAddDoc)
            } else {
                LazyColumn(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(docs) { doc ->
                        val days = daysUntil(doc.expiryDate)
                        val status = getDocumentStatus(doc.expiryDate)
                        val statusColor = when { days < 0 -> DangerRed; days < 30 -> WarningOrange; else -> SuccessGreen }

                        THCard(Modifier.fillMaxWidth(), onClick = { onDocDetail(doc.id) }) {
                            Row(Modifier.padding(14.dp)) {
                                StatusDot(statusColor, 10)
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(doc.type.label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text(doc.truckNumber, fontSize = 12.sp, color = TextSecondary)
                                    Text("Doc: ${doc.documentNumber}", fontSize = 11.sp, color = TextHint)
                                }
                                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                                    StatusBadge(status.label, statusColor)
                                    Spacer(Modifier.height(4.dp))
                                    Text(if (days < 0) "${-days} din expired" else "$days din baaki", fontSize = 11.sp, color = statusColor)
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}
