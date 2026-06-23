package com.truckhisaab.ui.screens.documents

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.DocumentStatus
import com.truckhisaab.data.model.getDocumentStatus
import com.truckhisaab.ui.components.ConfirmDialog
import com.truckhisaab.ui.components.StatusBadge
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.daysUntil
import com.truckhisaab.ui.components.formatDate
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.WarningOrange

@Composable
fun DocumentDetailScreen(docId: String, onBack: () -> Unit, viewModel: DocumentsViewModel = viewModel()) {
    val doc = viewModel.getDoc(docId) ?: run { onBack(); return }
    val status = getDocumentStatus(doc.expiryDate)
    val statusColor = when (status) { DocumentStatus.VALID -> SuccessGreen; DocumentStatus.EXPIRING_SOON -> WarningOrange; DocumentStatus.EXPIRED -> DangerRed }
    val days = daysUntil(doc.expiryDate)
    var showDelete by remember { mutableStateOf(false) }

    if (showDelete) {
        ConfirmDialog("Delete Document", "Pakka delete karna hai?", onConfirm = { viewModel.deleteDoc(docId); onBack() }, onDismiss = { showDelete = false })
    }

    Scaffold(
        topBar = {
            THTopBar(title = doc.type.label, onBack = onBack, containerColor = statusColor, actions = {
                IconButton(onClick = { showDelete = true }) { Icon(Icons.Default.Delete, "Delete", tint = Color.White) }
            })
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = statusColor.copy(0.1f))) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    StatusBadge(status.label, statusColor)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        when { days < 0 -> "${-days} din pehle expire ho gaya"; days == 0L -> "Aaj expire ho raha hai!"; else -> "$days din baaki" },
                        fontWeight = FontWeight.Bold, fontSize = 18.sp, color = statusColor
                    )
                }
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Document Details", fontWeight = FontWeight.Bold)
                    HorizontalDivider()
                    DetailRow("Type", doc.type.label)
                    DetailRow("Number", doc.documentNumber)
                    DetailRow("Truck", doc.truckNumber)
                    DetailRow("Issue Date", formatDate(doc.issueDate))
                    DetailRow("Expiry Date", formatDate(doc.expiryDate))
                    DetailRow("Reminder", "${doc.reminderDays.firstOrNull() ?: 30} din pehle")
                }
            }

            if (doc.renewalHistory.isNotEmpty()) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Renewal History", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        doc.renewalHistory.forEachIndexed { i, r ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Renewal ${i + 1}", fontSize = 13.sp)
                                Text(formatDate(r.date), fontSize = 13.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                com.truckhisaab.ui.components.THPrimaryButton("🔄 Renew Document", { }, Modifier.weight(1f), color = SuccessGreen)
                com.truckhisaab.ui.components.THPrimaryButton("📤 Share", { }, Modifier.weight(1f), color = com.truckhisaab.ui.theme.WhatsAppGreen)
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
