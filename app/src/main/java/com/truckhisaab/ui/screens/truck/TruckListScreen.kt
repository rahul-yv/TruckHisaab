package com.truckhisaab.ui.screens.truck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.theme.*

@Composable
fun TruckListScreen(onBack: () -> Unit, onAddTruck: () -> Unit, onTruckDetail: (Long) -> Unit, viewModel: TruckViewModel = hiltViewModel()) {
    val trucks by viewModel.trucks.collectAsState()

    Scaffold(
        topBar = { THTopBar("Trucks", onBack = onBack) },
        floatingActionButton = { QuickAddFab(onClick = onAddTruck) }
    ) { padding ->
        if (trucks.isEmpty()) {
            EmptyState(icon = Icons.Default.LocalShipping, title = "Koi truck nahi", actionLabel = "Truck Add", onAction = onAddTruck, modifier = Modifier.padding(padding))
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Spacer(Modifier.height(8.dp)) }
                items(trucks) { truck ->
                    THCard(Modifier.fillMaxWidth(), onClick = { onTruckDetail(truck.id) }) {
                        Row(Modifier.padding(14.dp)) {
                            Column(Modifier.weight(1f)) {
                                Text(truck.number, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("${truck.manufacturer} ${truck.model}", fontSize = 13.sp, color = TextSecondary)
                                Text("${truck.type.label} • ${truck.yearOfPurchase}", fontSize = 11.sp, color = TextHint)
                            }
                            Icon(Icons.Default.ChevronRight, null, tint = TextHint)
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
