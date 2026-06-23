package com.truckhisaab.ui.screens.driver

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.ui.components.ConfirmDialog
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.InfoBlue
import com.truckhisaab.ui.theme.SuccessGreen
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun DriverDetailScreen(driverId: String, onBack: () -> Unit, viewModel: DriverViewModel = viewModel()) {
    val driver = viewModel.getDriver(driverId) ?: run { onBack(); return }
    val trips = viewModel.getDriverTrips(driver.name)
    val totalFreight = trips.sumOf { it.freightAmount }
    var showDelete by remember { mutableStateOf(false) }

    if (showDelete) {
        ConfirmDialog("Delete Driver", "Pakka delete karna hai?", onConfirm = { viewModel.deleteDriver(driverId); onBack() }, onDismiss = { showDelete = false })
    }

    Scaffold(
        topBar = {
            THTopBar(title = driver.name, onBack = onBack, containerColor = InfoBlue, actions = {
                IconButton(onClick = { showDelete = true }) { Icon(Icons.Default.Delete, "Delete", tint = Color.White) }
            })
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Driver Info", fontWeight = FontWeight.Bold)
                    HorizontalDivider()
                    DetailRow("Name", driver.name)
                    DetailRow("Phone", driver.phone)
                    DetailRow("License", driver.licenseNumber)
                    if (driver.monthlySalary > 0) DetailRow("Salary", "${formatINR(driver.monthlySalary)}/month")
                    if (driver.advanceTaken > 0) DetailRow("Advance Taken", formatINR(driver.advanceTaken))
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Total Trips", "${trips.size}", TruckRed, Modifier.weight(1f))
                StatCard("Total Freight", formatINR(totalFreight), SuccessGreen, Modifier.weight(1f))
            }

            if (trips.isNotEmpty()) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Recent Trips", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        trips.take(5).forEach { t ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${t.fromLocation}→${t.toLocation}", fontSize = 13.sp)
                                Text(formatINR(t.freightAmount), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SuccessGreen)
                            }
                        }
                    }
                }
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

@Composable
private fun StatCard(label: String, value: String, color: Color, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
        Column(Modifier.padding(12.dp)) {
            Text(label, fontSize = 11.sp, color = TextSecondary)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = color)
        }
    }
}
