package com.truckhisaab.ui.screens.documents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.domain.model.Document
import com.truckhisaab.domain.model.DocumentStatus
import com.truckhisaab.domain.model.getDocumentStatus
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun DocumentItem(doc: Document, modifier: Modifier = Modifier) {
    val status = getDocumentStatus(doc.expiryDate)
    val statusColor = when (status) {
        DocumentStatus.VALID -> SuccessGreen
        DocumentStatus.EXPIRING_SOON -> WarningOrange
        DocumentStatus.EXPIRED -> DangerRed
    }
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
                    when {
                        days < 0 -> "${-days} din pehle expired"
                        days == 0L -> "Aaj expire"
                        else -> "$days din baaki"
                    },
                    fontSize = 11.sp, color = statusColor
                )
                Text("Exp: ${formatDate(doc.expiryDate)}", fontSize = 10.sp, color = TextHint)
            }
        }
    }
}
