package com.truckhisaab.ui.screens.driver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun DriverDetailScreen(driverId: Long, onBack: () -> Unit, viewModel: DriverViewModel = hiltViewModel()) {
    val drivers by viewModel.drivers.collectAsState()
    val driver = drivers.find { it.id == driverId }
    val trips by viewModel.getTripsForDriver(driverId).collectAsState()
    var showDelete by remember { mutableStateOf(false) }

    if (driver == null) { LoadingState(); return }

    if (showDelete) {
        ConfirmDialog("Delete Driver", "Pakka delete karna hai?", onConfirm = { viewModel.deleteDriver(driverId) { onBack() } }, onDismiss = { showDelete = false })
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

            val totalFreight = trips.sumOf { it.freightAmount }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatMiniCard("Total Trips", "${trips.size}", TruckRed, Modifier.weight(1f))
                StatMiniCard("Total Freight", formatINR(totalFreight), SuccessGreen, Modifier.weight(1f))
            }

            if (trips.isNotEmpty()) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Recent Trips", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        trips.take(5).forEach { t ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${t.fromLocation} → ${t.toLocation}", fontSize = 13.sp)
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
private fun StatMiniCard(label: String, value: String, color: Color, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
        Column(Modifier.padding(12.dp)) {
            Text(label, fontSize = 11.sp, color = TextSecondary)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = color)
        }
    }
}
