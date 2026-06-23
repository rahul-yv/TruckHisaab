package com.truckhisaab.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.truckhisaab.data.AppContainer
import com.truckhisaab.data.model.Trip
import com.truckhisaab.data.model.TripStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

data class PnLSummary(val label: String, val income: Double, val expense: Double, val changePercent: Double = 0.0) {
    val profit: Double get() = income - expense
    val isProfit: Boolean get() = profit >= 0
}

data class DashboardState(
    val todaySummary: PnLSummary = PnLSummary("Aaj ka Hisaab", 0.0, 0.0),
    val weeklySummary: PnLSummary = PnLSummary("Is Hafte", 0.0, 0.0),
    val monthlySummary: PnLSummary = PnLSummary("Is Mahine", 0.0, 0.0),
    val recentTrips: List<Trip> = emptyList(),
    val activeTripsCount: Int = 0,
    val totalTripsThisMonth: Int = 0,
    val totalKm: Int = 1240,
    val unreadNotifications: Int = 0,
    val dieselRate: Double = 96.50,
    val dieselRateChange: Double = 0.30,
    val expiringDocsCount: Int = 0
)

class DashboardViewModel : ViewModel() {
    private val tripRepo = AppContainer.tripRepository
    private val expenseRepo = AppContainer.expenseRepository
    private val docRepo = AppContainer.documentRepository
    private val userRepo = AppContainer.userRepository

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init { refreshData() }

    fun refreshData() {
        val todayIncome = tripRepo.getTodayIncome()
        val todayExpense = expenseRepo.getTodayExpense()
        val weekIncome = tripRepo.getWeekIncome()
        val weekExpense = expenseRepo.getWeekExpense()
        val monthIncome = tripRepo.getMonthIncome()
        val monthExpense = expenseRepo.getMonthExpense()

        val trips = tripRepo.trips.value
        val monthStart = System.currentTimeMillis() - 30 * 86400000L

        _state.value = DashboardState(
            todaySummary = PnLSummary("Aaj ka Hisaab", todayIncome, todayExpense),
            weeklySummary = PnLSummary("Is Hafte", weekIncome, weekExpense, 12.0),
            monthlySummary = PnLSummary("Is Mahine", monthIncome, monthExpense, 8.0),
            recentTrips = trips.sortedByDescending { it.startDate }.take(5),
            activeTripsCount = trips.count { it.status == TripStatus.ACTIVE },
            totalTripsThisMonth = trips.count { it.startDate >= monthStart },
            unreadNotifications = userRepo.unreadCount,
            expiringDocsCount = docRepo.getExpiringDocuments(15).size,
            dieselRate = 96.50,
            dieselRateChange = 0.30
        )
    }
}
