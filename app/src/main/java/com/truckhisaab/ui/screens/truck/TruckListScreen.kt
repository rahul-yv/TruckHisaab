package com.truckhisaab.ui.screens.truck

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.ui.components.EmptyState
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.theme.TextHint
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun TruckListScreen(onBack: () -> Unit, onAddTruck: () -> Unit, onTruckDetail: (String) -> Unit, viewModel: TruckViewModel = viewModel()) {
    val trucks by viewModel.trucks.collectAsState()

    Scaffold(
        topBar = { THTopBar(title = "Mere Trucks", onBack = onBack) },
        floatingActionButton = { FloatingActionButton(onClick = onAddTruck, containerColor = TruckRed, contentColor = Color.White) { Icon(Icons.Default.Add, "Naya Truck") } }
    ) { padding ->
        if (trucks.isEmpty()) {
            EmptyState(icon = Icons.Default.LocalShipping, title = "Koi truck nahi", actionLabel = "Naya Truck", onAction = onAddTruck, modifier = Modifier.fillMaxSize().padding(padding))
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(trucks) { truck ->
                    Card(Modifier.fillMaxWidth().clickable { onTruckDetail(truck.id) }, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, null, tint = TruckRed, modifier = Modifier.height(40.dp).width(40.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(truck.number, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("${truck.manufacturer} ${truck.model}", fontSize = 13.sp, color = TextSecondary)
                                Text("${truck.type.label} • ${truck.yearOfPurchase}", fontSize = 12.sp, color = TextHint)
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
