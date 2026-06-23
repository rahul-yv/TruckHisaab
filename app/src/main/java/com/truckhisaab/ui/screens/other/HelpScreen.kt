package com.truckhisaab.ui.screens.other

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.InfoBlue
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import com.truckhisaab.ui.theme.WhatsAppGreen

@Composable
fun HelpScreen(onBack: () -> Unit) {
    Scaffold(topBar = { THTopBar(title = "Help / Madad", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column {
                    ContactRow(Icons.Default.Phone, "Call Support", "1800-XXX-XXXX (Toll Free)", TruckRed)
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ContactRow(Icons.Default.Email, "WhatsApp Support", "+91 98XXX XXXXX", WhatsAppGreen)
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ContactRow(Icons.Default.Email, "Email Support", "help@truckhisaab.com", InfoBlue)
                }
            }

            Text("FAQ - Aksar Poochhe Jaane Wale Sawal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column {
                    FaqItem("Trip kaise add karein?", "Dashboard par 'Trip' button dabao, phir step by step details bharo - route, cargo, freight, driver.")
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    FaqItem("Kharcha kaise track karein?", "Trip ke dauran ya alag se - 'Kharcha' section mein jaake category chuno aur amount daalo.")
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    FaqItem("Document expire hone ka reminder kaise milega?", "Documents section mein document add karo aur reminder days set karo. App automatically yaad dilayega.")
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    FaqItem("P&L report kaise dekhein?", "Dashboard se 'Hisaab' button dabao. Date-wise, truck-wise, party-wise sab breakdown milega.")
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    FaqItem("Data safe hai?", "Haan, aapka data encrypted hai aur sirf aapke phone par stored hai. Cloud backup bhi available hai.")
                }
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(0.1f))) {
                Row(Modifier.padding(16.dp).clickable { }, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VideoLibrary, null, tint = SuccessGreen, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Video Tutorials", fontWeight = FontWeight.Bold, color = SuccessGreen)
                        Text("Hindi mein step-by-step videos", fontSize = 13.sp, color = TextSecondary)
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, detail: String, color: Color) {
    Row(Modifier.fillMaxWidth().clickable { }.padding(16.dp, 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(label, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(detail, fontSize = 13.sp, color = TextSecondary)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, tint = TextHint, modifier = Modifier.size(14.dp))
    }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth().clickable { expanded = !expanded }.padding(16.dp, 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.QuestionAnswer, null, tint = TruckRed, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(question, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Text(if (expanded) "▲" else "▼", color = TextHint)
        }
        if (expanded) {
            Spacer(Modifier.height(8.dp))
            Text(answer, fontSize = 13.sp, color = TextSecondary)
        }
    }
}
