package com.truckhisaab.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.*
import com.truckhisaab.domain.repository.*
import com.truckhisaab.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardState(
    val todayIncome: Double = 0.0, val todayExpense: Double = 0.0,
    val weekIncome: Double = 0.0, val weekExpense: Double = 0.0,
    val monthIncome: Double = 0.0, val monthExpense: Double = 0.0,
    val recentTrips: List<Trip> = emptyList(),
    val expiringDocs: List<Document> = emptyList(),
    val truckCount: Int = 0, val driverCount: Int = 0, val tripCount: Int = 0,
    val unreadNotifications: Int = 0, val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tripRepo: TripRepository,
    private val expenseRepo: ExpenseRepository,
    private val truckRepo: TruckRepository,
    private val driverRepo: DriverRepository,
    private val documentRepo: DocumentRepository,
    private val notificationRepo: NotificationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init { loadDashboard() }

    fun loadDashboard() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            tripRepo.getIncomeSince(startOfToday()).onEach { v -> _state.update { it.copy(todayIncome = v) } }.launchIn(viewModelScope)
            expenseRepo.getExpenseSince(startOfToday()).onEach { v -> _state.update { it.copy(todayExpense = v) } }.launchIn(viewModelScope)
            tripRepo.getIncomeSince(startOfWeek()).onEach { v -> _state.update { it.copy(weekIncome = v) } }.launchIn(viewModelScope)
            expenseRepo.getExpenseSince(startOfWeek()).onEach { v -> _state.update { it.copy(weekExpense = v) } }.launchIn(viewModelScope)
            tripRepo.getIncomeSince(startOfMonth()).onEach { v -> _state.update { it.copy(monthIncome = v) } }.launchIn(viewModelScope)
            expenseRepo.getExpenseSince(startOfMonth()).onEach { v -> _state.update { it.copy(monthExpense = v) } }.launchIn(viewModelScope)
            tripRepo.getAllTrips().onEach { trips -> _state.update { it.copy(recentTrips = trips.take(5)) } }.launchIn(viewModelScope)
            documentRepo.getExpiringDocuments(30).onEach { docs -> _state.update { it.copy(expiringDocs = docs) } }.launchIn(viewModelScope)
            truckRepo.getTruckCount().onEach { c -> _state.update { it.copy(truckCount = c) } }.launchIn(viewModelScope)
            driverRepo.getDriverCount().onEach { c -> _state.update { it.copy(driverCount = c) } }.launchIn(viewModelScope)
            tripRepo.getTripCount().onEach { c -> _state.update { it.copy(tripCount = c) } }.launchIn(viewModelScope)
            notificationRepo.getUnreadCount().onEach { c -> _state.update { it.copy(unreadNotifications = c) } }.launchIn(viewModelScope)
            _state.update { it.copy(isLoading = false) }
        }
    }
}
