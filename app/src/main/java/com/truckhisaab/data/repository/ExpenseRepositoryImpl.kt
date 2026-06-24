package com.truckhisaab.data.repository

import com.truckhisaab.data.local.dao.ExpenseDao
import com.truckhisaab.data.local.dao.FuelEntryDao
import com.truckhisaab.data.mapper.toDomain
import com.truckhisaab.data.mapper.toEntity
import com.truckhisaab.domain.model.CategoryBreakdown
import com.truckhisaab.domain.model.Expense
import com.truckhisaab.domain.model.ExpenseCategory
import com.truckhisaab.domain.model.FuelEntry
import com.truckhisaab.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val fuelEntryDao: FuelEntryDao
) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> =
        expenseDao.getAllExpenses().map { list -> list.map { it.toDomain() } }

    override fun getExpensesForTrip(tripId: Long): Flow<List<Expense>> =
        expenseDao.getExpensesForTrip(tripId).map { list -> list.map { it.toDomain() } }

    override fun getExpensesForTruck(truckId: Long): Flow<List<Expense>> =
        expenseDao.getExpensesForTruck(truckId).map { list -> list.map { it.toDomain() } }

    override fun getExpensesByCategory(category: String): Flow<List<Expense>> =
        expenseDao.getExpensesByCategory(category).map { list -> list.map { it.toDomain() } }

    override fun getExpenseSince(since: Long): Flow<Double> =
        expenseDao.getExpenseSince(since)

    override fun getCategoryBreakdown(since: Long): Flow<List<CategoryBreakdown>> =
        expenseDao.getCategoryBreakdown(since).map { list ->
            val total = list.sumOf { it.total }
            list.map { sum ->
                CategoryBreakdown(
                    category = ExpenseCategory.entries.find { it.name == sum.category } ?: ExpenseCategory.OTHER,
                    total = sum.total,
                    count = sum.count,
                    percentage = if (total > 0) (sum.total / total) * 100 else 0.0
                )
            }
        }

    override fun getAllFuelEntries(): Flow<List<FuelEntry>> =
        fuelEntryDao.getAllFuelEntries().map { list -> list.map { it.toDomain() } }

    override suspend fun getExpenseById(id: Long): Expense? =
        expenseDao.getExpenseById(id)?.toDomain()

    override suspend fun addExpense(expense: Expense): Long =
        expenseDao.insert(expense.toEntity())

    override suspend fun updateExpense(expense: Expense) =
        expenseDao.update(expense.toEntity())

    override suspend fun deleteExpense(id: Long) =
        expenseDao.deleteById(id)

    override suspend fun addFuelEntry(entry: FuelEntry): Long =
        fuelEntryDao.insert(entry.toEntity())
}
