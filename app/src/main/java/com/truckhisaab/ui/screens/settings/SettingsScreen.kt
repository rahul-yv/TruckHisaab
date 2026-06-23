package com.truckhisaab.ui.screens.settings

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var notifTrip by remember { mutableStateOf(true) }
    var notifDoc by remember { mutableStateOf(true) }
    var notifExpense by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(false) }
    var largeText by remember { mutableStateOf(false) }

    Scaffold(topBar = { THTopBar(title = "Settings", onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SectionCard("Notifications") {
                ToggleRow("Trip Updates", "Trip start/end ki notification", notifTrip) { notifTrip = it }
                HorizontalDivider()
                ToggleRow("Document Expiry", "Document expire hone se pehle", notifDoc) { notifDoc = it }
                HorizontalDivider()
                ToggleRow("Daily Expense Summary", "Roz ka kharcha summary", notifExpense) { notifExpense = it }
            }

            SectionCard("Display") {
                ToggleRow("Dark Mode", "Raat ko aankho ko aaram", darkMode) { darkMode = it }
                HorizontalDivider()
                ToggleRow("Bada Text", "Bade akshar mein dikhaye", largeText) { largeText = it }
            }

            SectionCard("Language / Bhasha") {
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Bhasha", fontWeight = FontWeight.Medium)
                    Text("Hindi + English", color = TruckRed, fontWeight = FontWeight.SemiBold)
                }
            }

            SectionCard("Data & Storage") {
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Data Used", fontWeight = FontWeight.Medium)
                    Text("12.4 MB", color = TextSecondary)
                }
                HorizontalDivider()
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Cache Clear", fontWeight = FontWeight.Medium)
                    Text("Tap to clear", color = TruckRed)
                }
            }

            SectionCard("About") {
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Version", fontWeight = FontWeight.Medium)
                    Text("1.0.0", color = TextSecondary)
                }
                HorizontalDivider()
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Privacy Policy", fontWeight = FontWeight.Medium)
                    Text("→", color = TruckRed)
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(top = 14.dp)) {
            Text(title, Modifier.padding(horizontal = 14.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun ToggleRow(title: String, subtitle: String, checked: Boolean, onChanged: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(14.dp, 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            Text(subtitle, fontSize = 12.sp, color = TextSecondary)
        }
        Switch(checked, onCheckedChange = onChanged, colors = SwitchDefaults.colors(checkedTrackColor = TruckRed))
    }
}
