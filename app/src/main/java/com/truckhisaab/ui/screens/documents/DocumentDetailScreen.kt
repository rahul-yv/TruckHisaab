package com.truckhisaab.ui.screens.documents

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
fun DocumentDetailScreen(docId: Long, onBack: () -> Unit, viewModel: DocumentsViewModel = hiltViewModel()) {
    val docs by viewModel.documents.collectAsState()
    val doc = docs.find { it.id == docId }
    var showDelete by remember { mutableStateOf(false) }

    if (doc == null) { LoadingState(); return }

    val days = daysUntil(doc.expiryDate)
    val status = getDocumentStatus(doc.expiryDate)
    val statusColor = when { days < 0 -> DangerRed; days < 30 -> WarningOrange; else -> SuccessGreen }

    Scaffold(topBar = {
        THTopBar("Document Detail", onBack = onBack) {
            IconButton(onClick = { showDelete = true }) { Icon(Icons.Default.Delete, "Delete", tint = Color.White) }
        }
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            HeroCard(doc.type.label, doc.documentNumber, subtitle = doc.truckNumber)
            THCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Row(Modifier.fillMaxWidth()) {
                        Text("Status", Modifier.weight(1f), color = TextSecondary)
                        StatusBadge(status.label, statusColor)
                    }
                    Spacer(Modifier.height(8.dp))
                    DetailRow("Truck", doc.truckNumber)
                    DetailRow("Issue Date", formatDate(doc.issueDate))
                    DetailRow("Expiry Date", formatDate(doc.expiryDate))
                    DetailRow("Remaining", if (days < 0) "${-days} din expired" else "$days din")
                    DetailRow("Reminders", doc.reminderDays.joinToString(", ") { "$it din" })
                }
            }
        }
    }

    if (showDelete) {
        ConfirmDialog("Delete Document?", "Kya aap ye document delete karna chahte hain?", onConfirm = { viewModel.deleteDocument(docId); onBack() }, onDismiss = { showDelete = false })
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(label, Modifier.width(100.dp), color = TextSecondary, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}
