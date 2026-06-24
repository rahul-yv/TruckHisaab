package com.truckhisaab.ui.screens.trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.truckhisaab.domain.model.TripStatus
import com.truckhisaab.ui.components.*
import com.truckhisaab.ui.screens.dashboard.TripListItem
import com.truckhisaab.ui.theme.TruckRed

@Composable
fun TripListScreen(
    onBack: () -> Unit, onAddTrip: () -> Unit, onTripDetail: (Long) -> Unit,
    viewModel: TripViewModel = hiltViewModel()
) {
    val trips by viewModel.filteredTrips.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val query by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = { THTopBar("Trips", onBack = onBack) },
        floatingActionButton = { QuickAddFab(onClick = onAddTrip) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            THSearchBar(query, onQueryChange = { viewModel.updateSearch(it) }, placeholder = "Trip search...", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            LazyRow(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf("ALL" to "Sab") + TripStatus.entries.map { it.name to it.hindiLabel }
                items(filters) { (key, label) ->
                    FilterChip(selected = filter == key, onClick = { viewModel.updateFilter(key) }, label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TruckRed, selectedLabelColor = androidx.compose.ui.graphics.Color.White))
                }
            }
            if (trips.isEmpty()) {
                EmptyState(icon = Icons.Default.LocalShipping, title = "Koi trip nahi", subtitle = "Naya trip add karein", actionLabel = "Trip Add Karein", onAction = onAddTrip)
            } else {
                LazyColumn(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Spacer(Modifier.height(8.dp)) }
                    items(trips) { trip -> TripListItem(trip = trip, onClick = { onTripDetail(trip.id) }) }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}
