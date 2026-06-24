package com.truckhisaab.ui.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.*
import com.truckhisaab.domain.repository.*
import com.truckhisaab.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReportState(
    val period: String = "month",
    val totalIncome: Double = 0.0, val totalExpense: Double = 0.0,
    val categoryBreakdown: List<CategoryBreakdown> = emptyList(),
    val truckBreakdown: List<TruckPnL> = emptyList(),
    val partyBreakdown: List<PartyBreakdown> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val tripRepo: TripRepository,
    private val expenseRepo: ExpenseRepository,
    private val truckRepo: TruckRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReportState())
    val state: StateFlow<ReportState> = _state.asStateFlow()

    init { loadReport("month") }

    fun loadReport(period: String) {
        val since = when (period) {
            "today" -> startOfToday()
            "week" -> startOfWeek()
            "month" -> startOfMonth()
            "year" -> startOfYear()
            else -> startOfMonth()
        }
        _state.update { it.copy(period = period, isLoading = true) }

        tripRepo.getIncomeSince(since).onEach { v -> _state.update { it.copy(totalIncome = v) } }.launchIn(viewModelScope)
        expenseRepo.getExpenseSince(since).onEach { v -> _state.update { it.copy(totalExpense = v) } }.launchIn(viewModelScope)
        expenseRepo.getCategoryBreakdown(since).onEach { v -> _state.update { it.copy(categoryBreakdown = v) } }.launchIn(viewModelScope)

        viewModelScope.launch {
            combine(tripRepo.getAllTrips(), expenseRepo.getAllExpenses(), truckRepo.getAllTrucks()) { trips, expenses, trucks ->
                val filtered = trips.filter { it.startDate >= since && it.status != TripStatus.CANCELLED }
                val truckPnl = trucks.map { truck ->
                    val tTrips = filtered.filter { it.truckId == truck.id }
                    val tIncome = tTrips.sumOf { it.freightAmount }
                    val tExpense = expenses.filter { it.truckId == truck.id && it.date >= since }.sumOf { it.amount }
                    TruckPnL(truck.number, tIncome, tExpense, tIncome - tExpense, tTrips.size)
                }.filter { it.trips > 0 }

                val partyPnl = filtered.groupBy { it.partyName }.map { (party, pTrips) ->
                    PartyBreakdown(party.ifBlank { "Unknown" }, pTrips.sumOf { it.freightAmount }, pTrips.size)
                }.sortedByDescending { it.totalAmount }

                _state.update { it.copy(truckBreakdown = truckPnl, partyBreakdown = partyPnl, isLoading = false) }
            }.collect()
        }
    }
}
