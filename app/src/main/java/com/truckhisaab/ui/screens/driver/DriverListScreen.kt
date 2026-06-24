package com.truckhisaab.ui.screens.driver

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.ui.components.EmptyState
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.components.formatINR
import com.truckhisaab.ui.theme.*

@Composable
fun DriverListScreen(onBack: () -> Unit, onAddDriver: () -> Unit, onDriverDetail: (Long) -> Unit, viewModel: DriverViewModel = hiltViewModel()) {
    val drivers by viewModel.drivers.collectAsState()

    Scaffold(
        topBar = { THTopBar(title = "Mere Drivers", onBack = onBack) },
        floatingActionButton = { FloatingActionButton(onClick = onAddDriver, containerColor = TruckRed, contentColor = Color.White) { Icon(Icons.Default.Add, "Naya Driver") } }
    ) { padding ->
        if (drivers.isEmpty()) {
            EmptyState(icon = Icons.Default.Person, title = "Koi driver nahi", actionLabel = "Naya Driver", onAction = onAddDriver, modifier = Modifier.fillMaxSize().padding(padding))
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(drivers) { driver ->
                    Card(Modifier.fillMaxWidth().clickable { onDriverDetail(driver.id) }, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = InfoBlue, modifier = Modifier.size(40.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(driver.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Phone, null, tint = TextHint, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text(driver.phone, fontSize = 13.sp, color = TextSecondary)
                                }
                                if (driver.monthlySalary > 0) Text("Salary: ${formatINR(driver.monthlySalary)}/month", fontSize = 12.sp, color = TextHint)
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
