package com.truckhisaab.ui.screens.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.*
import com.truckhisaab.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTripState(
    val step: Int = 0, val from: String = "", val to: String = "",
    val cargoType: String = "", val weight: String = "",
    val freight: String = "", val advance: String = "", val party: String = "",
    val selectedTruckId: Long = 0, val selectedTruckNumber: String = "",
    val selectedDriverId: Long = 0, val selectedDriverName: String = "",
    val isSaving: Boolean = false
)

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepo: TripRepository,
    private val truckRepo: TruckRepository,
    private val driverRepo: DriverRepository,
    private val expenseRepo: ExpenseRepository
) : ViewModel() {

    val trips = tripRepo.getAllTrips().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val trucks = truckRepo.getAllTrucks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val drivers = driverRepo.getAllDrivers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _addState = MutableStateFlow(AddTripState())
    val addState: StateFlow<AddTripState> = _addState.asStateFlow()

    private val _filter = MutableStateFlow("ALL")
    val filter: StateFlow<String> = _filter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredTrips = combine(trips, _filter, _searchQuery) { list, f, q ->
        list.filter { trip ->
            val matchFilter = f == "ALL" || trip.status.name == f
            val matchQuery = q.isBlank() || trip.fromLocation.contains(q, true) || trip.toLocation.contains(q, true) || trip.partyName.contains(q, true)
            matchFilter && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateFilter(f: String) { _filter.value = f }
    fun updateSearch(q: String) { _searchQuery.value = q }
    fun updateAddState(update: AddTripState.() -> AddTripState) { _addState.update { it.update() } }
    fun resetAddState() { _addState.value = AddTripState() }

    fun saveTrip(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _addState.update { it.copy(isSaving = true) }
            val s = _addState.value
            tripRepo.addTrip(Trip(
                fromLocation = s.from, toLocation = s.to, cargoType = s.cargoType,
                weightTons = s.weight.toDoubleOrNull() ?: 0.0,
                freightAmount = s.freight.toDoubleOrNull() ?: 0.0,
                advanceAmount = s.advance.toDoubleOrNull() ?: 0.0,
                partyName = s.party, truckId = s.selectedTruckId,
                truckNumber = s.selectedTruckNumber, driverId = s.selectedDriverId,
                driverName = s.selectedDriverName, status = TripStatus.ACTIVE
            ))
            _addState.update { it.copy(isSaving = false) }
            resetAddState()
            onSuccess()
        }
    }

    fun completeTrip(tripId: Long, onDone: () -> Unit) {
        viewModelScope.launch {
            tripRepo.getTripById(tripId)?.let { trip ->
                tripRepo.updateTrip(trip.copy(status = TripStatus.COMPLETED, endDate = System.currentTimeMillis()))
            }
            onDone()
        }
    }

    fun deleteTrip(tripId: Long, onDone: () -> Unit) {
        viewModelScope.launch {
            tripRepo.deleteTrip(tripId)
            onDone()
        }
    }

    fun getExpensesForTrip(tripId: Long) = expenseRepo.getExpensesForTrip(tripId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
