package com.truckhisaab.domain.usecase

import com.truckhisaab.domain.model.*
import com.truckhisaab.domain.repository.ExpenseRepository
import com.truckhisaab.domain.repository.TripRepository
import com.truckhisaab.domain.repository.TruckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetPnLReportUseCase @Inject constructor(
    private val tripRepo: TripRepository,
    private val expenseRepo: ExpenseRepository
) {
    fun getIncome(since: Long): Flow<Double> = tripRepo.getIncomeSince(since)
    fun getExpense(since: Long): Flow<Double> = expenseRepo.getExpenseSince(since)
}

class GetTruckReportUseCase @Inject constructor(
    private val tripRepo: TripRepository,
    private val expenseRepo: ExpenseRepository,
    private val truckRepo: TruckRepository
) {
    fun invoke(since: Long): Flow<List<TruckPnL>> =
        combine(tripRepo.getAllTrips(), expenseRepo.getAllExpenses(), truckRepo.getAllTrucks()) { trips, expenses, trucks ->
            val filtered = trips.filter { it.startDate >= since && it.status != TripStatus.CANCELLED }
            trucks.map { truck ->
                val tTrips = filtered.filter { it.truckId == truck.id }
                val tIncome = tTrips.sumOf { it.freightAmount }
                val tExpense = expenses.filter { it.truckId == truck.id && it.date >= since }.sumOf { it.amount }
                TruckPnL(truck.number, tIncome, tExpense, tIncome - tExpense, tTrips.size)
            }.filter { it.trips > 0 }
        }
}

class GetPartyReportUseCase @Inject constructor(private val tripRepo: TripRepository) {
    fun invoke(since: Long): Flow<List<PartyBreakdown>> =
        tripRepo.getAllTrips().combine(kotlinx.coroutines.flow.flowOf(since)) { trips, s ->
            trips.filter { it.startDate >= s && it.status != TripStatus.CANCELLED }
                .groupBy { it.partyName }
                .map { (party, pTrips) ->
                    PartyBreakdown(party.ifBlank { "Unknown" }, pTrips.sumOf { it.freightAmount }, pTrips.size)
                }.sortedByDescending { it.totalAmount }
        }
}

class GetDriverReportUseCase @Inject constructor(private val tripRepo: TripRepository) {
    fun invoke(since: Long): Flow<List<Pair<String, Int>>> =
        tripRepo.getAllTrips().combine(kotlinx.coroutines.flow.flowOf(since)) { trips, s ->
            trips.filter { it.startDate >= s && it.status != TripStatus.CANCELLED }
                .groupBy { it.driverName }
                .map { (name, t) -> (name.ifBlank { "Unknown" }) to t.size }
                .sortedByDescending { it.second }
        }
}

class GetNotificationUseCases @Inject constructor(
    private val repo: com.truckhisaab.domain.repository.NotificationRepository
) {
    fun getAll(): Flow<List<AppNotification>> = repo.getAllNotifications()
    fun getUnreadCount(): Flow<Int> = repo.getUnreadCount()
    suspend fun markRead(id: Long) = repo.markAsRead(id)
    suspend fun markAllRead() = repo.markAllAsRead()
}
