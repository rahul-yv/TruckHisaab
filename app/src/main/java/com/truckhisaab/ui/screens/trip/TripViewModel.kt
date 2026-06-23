package com.truckhisaab.ui.screens.trip

import androidx.lifecycle.ViewModel
import com.truckhisaab.data.AppContainer
import com.truckhisaab.data.model.Trip
import com.truckhisaab.data.model.TripStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddTripState(
    val step: Int = 1,
    val from: String = "",
    val to: String = "",
    val cargoType: String = "",
    val weight: String = "",
    val freight: String = "",
    val advance: String = "",
    val partyName: String = "",
    val driverName: String = "",
    val truckNumber: String = "",
    val showSuccess: Boolean = false
)

data class TripListState(
    val filter: TripStatus? = null,
    val searchQuery: String = ""
)

class TripViewModel : ViewModel() {
    private val repo = AppContainer.tripRepository
    val trips = repo.trips

    private val _addState = MutableStateFlow(AddTripState())
    val addState: StateFlow<AddTripState> = _addState.asStateFlow()

    private val _listState = MutableStateFlow(TripListState())
    val listState: StateFlow<TripListState> = _listState.asStateFlow()

    fun filteredTrips(): List<Trip> {
        val s = _listState.value
        var list = trips.value
        if (s.filter != null) list = list.filter { it.status == s.filter }
        if (s.searchQuery.isNotBlank()) {
            val q = s.searchQuery.lowercase()
            list = list.filter {
                it.fromLocation.lowercase().contains(q) || it.toLocation.lowercase().contains(q) ||
                it.partyName.lowercase().contains(q) || it.cargoType.lowercase().contains(q)
            }
        }
        return list.sortedByDescending { it.startDate }
    }

    fun setFilter(status: TripStatus?) = _listState.update { it.copy(filter = status) }
    fun setSearch(query: String) = _listState.update { it.copy(searchQuery = query) }

    fun updateAddField(field: String, value: String) {
        _addState.update {
            when (field) {
                "from" -> it.copy(from = value)
                "to" -> it.copy(to = value)
                "cargo" -> it.copy(cargoType = value)
                "weight" -> it.copy(weight = value)
                "freight" -> it.copy(freight = value)
                "advance" -> it.copy(advance = value)
                "party" -> it.copy(partyName = value)
                "driver" -> it.copy(driverName = value)
                "truck" -> it.copy(truckNumber = value)
                else -> it
            }
        }
    }

    fun nextStep() = _addState.update { it.copy(step = (it.step + 1).coerceAtMost(5)) }
    fun prevStep() = _addState.update { it.copy(step = (it.step - 1).coerceAtLeast(1)) }

    fun canProceed(): Boolean {
        val s = _addState.value
        return when (s.step) {
            1 -> s.from.isNotBlank() && s.to.isNotBlank()
            2 -> s.cargoType.isNotBlank()
            3 -> s.freight.isNotBlank()
            4 -> s.driverName.isNotBlank() || s.truckNumber.isNotBlank()
            else -> true
        }
    }

    fun saveTrip() {
        val s = _addState.value
        repo.addTrip(Trip(
            fromLocation = s.from, toLocation = s.to, cargoType = s.cargoType,
            weightTons = s.weight.toDoubleOrNull() ?: 0.0,
            freightAmount = s.freight.toDoubleOrNull() ?: 0.0,
            advanceAmount = s.advance.toDoubleOrNull() ?: 0.0,
            partyName = s.partyName, driverName = s.driverName, truckNumber = s.truckNumber,
            status = TripStatus.ACTIVE
        ))
        _addState.value = AddTripState(showSuccess = true)
    }

    fun resetAdd() { _addState.value = AddTripState() }
    fun dismissSuccess() = _addState.update { it.copy(showSuccess = false) }

    fun completeTrip(tripId: String) = repo.completeTrip(tripId)
    fun deleteTrip(tripId: String) = repo.deleteTrip(tripId)

    fun getTrip(tripId: String): Trip? = repo.getTrip(tripId)
    fun getTripExpenses(tripId: String) = AppContainer.expenseRepository.getExpensesForTrip(tripId)
}
