package com.truckhisaab.domain.usecase

import com.truckhisaab.domain.model.CategoryBreakdown
import com.truckhisaab.domain.model.Expense
import com.truckhisaab.domain.model.FuelEntry
import com.truckhisaab.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(private val repo: ExpenseRepository) {
    operator fun invoke(): Flow<List<Expense>> = repo.getAllExpenses()
    fun forTrip(tripId: Long): Flow<List<Expense>> = repo.getExpensesForTrip(tripId)
    fun forTruck(truckId: Long): Flow<List<Expense>> = repo.getExpensesForTruck(truckId)
    fun byCategory(category: String): Flow<List<Expense>> = repo.getExpensesByCategory(category)
}

class AddExpenseUseCase @Inject constructor(private val repo: ExpenseRepository) {
    suspend operator fun invoke(expense: Expense): Long = repo.addExpense(expense)
}

class DeleteExpenseUseCase @Inject constructor(private val repo: ExpenseRepository) {
    suspend operator fun invoke(id: Long) = repo.deleteExpense(id)
}

class GetExpenseAnalyticsUseCase @Inject constructor(private val repo: ExpenseRepository) {
    fun getCategoryBreakdown(since: Long): Flow<List<CategoryBreakdown>> = repo.getCategoryBreakdown(since)
    fun getTotalSince(since: Long): Flow<Double> = repo.getExpenseSince(since)
    fun getFuelEntries(): Flow<List<FuelEntry>> = repo.getAllFuelEntries()
}
