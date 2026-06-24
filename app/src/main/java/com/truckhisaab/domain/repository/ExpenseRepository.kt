package com.truckhisaab.domain.repository

import com.truckhisaab.domain.model.CategoryBreakdown
import com.truckhisaab.domain.model.Expense
import com.truckhisaab.domain.model.FuelEntry
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesForTrip(tripId: Long): Flow<List<Expense>>
    fun getExpensesForTruck(truckId: Long): Flow<List<Expense>>
    fun getExpensesByCategory(category: String): Flow<List<Expense>>
    fun getExpenseSince(since: Long): Flow<Double>
    fun getCategoryBreakdown(since: Long): Flow<List<CategoryBreakdown>>
    fun getAllFuelEntries(): Flow<List<FuelEntry>>
    suspend fun getExpenseById(id: Long): Expense?
    suspend fun addExpense(expense: Expense): Long
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(id: Long)
    suspend fun addFuelEntry(entry: FuelEntry): Long
}
