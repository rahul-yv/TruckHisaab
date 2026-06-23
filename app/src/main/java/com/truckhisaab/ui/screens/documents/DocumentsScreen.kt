package com.truckhisaab.ui.screens.documents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.Document
import com.truckhisaab.data.model.DocumentStatus
import com.truckhisaab.data.model.getDocumentStatus
import com.truckhisaab.ui.components.EmptyState
import com.truckhisaab.ui.components.StatusBadge
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.daysUntil
import com.truckhisaab.ui.components.formatDate
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.WarningOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentListScreen(
    onBack: () -> Unit,
    onAddDoc: () -> Unit,
    onDocDetail: (String) -> Unit,
    viewModel: DocumentsViewModel = viewModel()
) {
    val truckFilter by viewModel.truckFilter.collectAsState()
    val trucks = com.truckhisaab.data.AppContainer.truckRepository.trucks.value
    val expiringCount = viewModel.getExpiringDocs().size

    Scaffold(
        topBar = { THTopBar(title = "Documents", onBack = onBack) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDoc, containerColor = TruckRed, contentColor = Color.White) {
                Icon(Icons.Default.Add, "Naya Document")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (expiringCount > 0) {
                Card(Modifier.fillMaxWidth().padding(16.dp, 8.dp), shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = DangerRed.copy(0.1f))) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, null, tint = DangerRed, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("$expiringCount documents jaldi expire honge!", fontSize = 13.sp, color = DangerRed, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp).horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = truckFilter == null, onClick = { viewModel.setTruckFilter(null) }, label = { Text("Sab Trucks") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                trucks.forEach { t ->
                    FilterChip(selected = truckFilter == t.number, onClick = { viewModel.setTruckFilter(t.number) }, label = { Text(t.number) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                }
            }

            Spacer(Modifier.height(8.dp))
            val docs = viewModel.filteredDocs()
            if (docs.isEmpty()) {
                EmptyState(icon = Icons.Default.Description, title = "Koi document nahi", actionLabel = "Naya Document", onAction = onAddDoc)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(docs) { doc -> DocumentItem(doc, Modifier.clickable { onDocDetail(doc.id) }) }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun DocumentItem(doc: Document, modifier: Modifier = Modifier) {
    val status = getDocumentStatus(doc.expiryDate)
    val statusColor = when (status) { DocumentStatus.VALID -> SuccessGreen; DocumentStatus.EXPIRING_SOON -> WarningOrange; DocumentStatus.EXPIRED -> DangerRed }
    val days = daysUntil(doc.expiryDate)

    Card(modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(44.dp).clip(CircleShape).background(statusColor.copy(0.15f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Description, null, tint = statusColor, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(doc.type.label, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(doc.documentNumber, fontSize = 12.sp, color = TextSecondary)
                Text("Truck: ${doc.truckNumber}", fontSize = 11.sp, color = TextHint)
            }
            Column(horizontalAlignment = Alignment.End) {
                StatusBadge(status.label, statusColor)
                Spacer(Modifier.height(4.dp))
                Text(
                    when { days < 0 -> "${-days} din pehle expired"; days == 0L -> "Aaj expire"; else -> "$days din baaki" },
                    fontSize = 11.sp, color = statusColor
                )
                Text("Exp: ${formatDate(doc.expiryDate)}", fontSize = 10.sp, color = TextHint)
            }
        }
    }
}
