package com.truckhisaab.ui.screens.trip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.truckhisaab.data.model.TripStatus
import com.truckhisaab.ui.components.EmptyState
import com.truckhisaab.ui.components.THTopBar
import com.truckhisaab.ui.screens.dashboard.TripListItem
import com.truckhisaab.ui.theme.TruckRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
    onBack: () -> Unit,
    onAddTrip: () -> Unit,
    onTripDetail: (String) -> Unit,
    onActiveTrip: (String) -> Unit,
    viewModel: TripViewModel = viewModel()
) {
    val listState by viewModel.listState.collectAsState()
    val trips by viewModel.trips.collectAsState()
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            THTopBar(title = "Meri Trips", onBack = onBack, actions = {
                IconButton(onClick = { showSearch = !showSearch }) {
                    Icon(Icons.Default.Search, "Search", tint = Color.White)
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTrip, containerColor = TruckRed, contentColor = Color.White) {
                Icon(Icons.Default.Add, "Naya Trip")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (showSearch) {
                OutlinedTextField(
                    value = listState.searchQuery,
                    onValueChange = viewModel::setSearch,
                    modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp),
                    placeholder = { Text("Route, party, cargo search karein...") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, null) }
                )
            }

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp).horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = listState.filter == null, onClick = { viewModel.setFilter(null) }, label = { Text("Sab") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                TripStatus.entries.forEach { status ->
                    FilterChip(selected = listState.filter == status, onClick = { viewModel.setFilter(status) }, label = { Text(status.hindiLabel) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = Color.White))
                }
            }

            Spacer(Modifier.height(8.dp))

            val filtered = viewModel.filteredTrips()
            if (filtered.isEmpty()) {
                EmptyState(icon = Icons.Default.LocalShipping, title = "Koi trip nahi", subtitle = "Naya trip shuru karein", actionLabel = "Naya Trip", onAction = onAddTrip)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(filtered) { trip ->
                        TripListItem(trip, Modifier.clickable {
                            if (trip.status == TripStatus.ACTIVE) onActiveTrip(trip.id) else onTripDetail(trip.id)
                        })
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}
