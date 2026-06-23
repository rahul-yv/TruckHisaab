package com.truckhisaab.data.repository

import com.truckhisaab.data.model.Expense
import com.truckhisaab.data.model.ExpenseCategory
import com.truckhisaab.data.model.FuelEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExpenseRepository {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _fuelEntries = MutableStateFlow<List<FuelEntry>>(emptyList())
    val fuelEntries: StateFlow<List<FuelEntry>> = _fuelEntries.asStateFlow()

    init { seedData() }

    fun addExpense(expense: Expense) = _expenses.update { it + expense }
    fun deleteExpense(id: String) = _expenses.update { it.filter { e -> e.id != id } }
    fun getExpense(id: String): Expense? = _expenses.value.find { it.id == id }
    fun getExpensesForTrip(tripId: String): List<Expense> = _expenses.value.filter { it.tripId == tripId }
    fun getExpensesForTruck(truckId: String): List<Expense> = _expenses.value.filter { it.truckId == truckId }

    fun addFuelEntry(entry: FuelEntry) = _fuelEntries.update { it + entry }
    fun getFuelEntriesForTruck(truckId: String): List<FuelEntry> = _fuelEntries.value.filter { it.truckId == truckId }

    fun getTotalExpense(): Double = _expenses.value.sumOf { it.amount }
    fun getTodayExpense(): Double {
        val todayStart = System.currentTimeMillis() - (System.currentTimeMillis() % 86400000L)
        return _expenses.value.filter { it.date >= todayStart }.sumOf { it.amount }
    }
    fun getWeekExpense(): Double {
        val weekStart = System.currentTimeMillis() - 7 * 86400000L
        return _expenses.value.filter { it.date >= weekStart }.sumOf { it.amount }
    }
    fun getMonthExpense(): Double {
        val monthStart = System.currentTimeMillis() - 30 * 86400000L
        return _expenses.value.filter { it.date >= monthStart }.sumOf { it.amount }
    }

    fun getCategoryBreakdown(): Map<ExpenseCategory, Double> =
        _expenses.value.groupBy { it.category }.mapValues { (_, exps) -> exps.sumOf { it.amount } }

    private fun seedData() {
        val day = 86400000L
        val now = System.currentTimeMillis()
        _expenses.value = listOf(
            Expense(tripId = "t1", category = ExpenseCategory.DIESEL, amount = 8500.0, date = now - 2 * day, location = "Shell Pump, Mumbai-Pune Highway", note = "Full tank"),
            Expense(tripId = "t1", category = ExpenseCategory.TOLL, amount = 1200.0, date = now - 2 * day, location = "Mumbai-Pune Expressway"),
            Expense(tripId = "t1", category = ExpenseCategory.FOOD, amount = 350.0, date = now - 2 * day, location = "Highway Dhaba"),
            Expense(tripId = "t2", category = ExpenseCategory.DIESEL, amount = 12000.0, date = now - day, location = "HP Petrol Pump, NH48"),
            Expense(tripId = "t2", category = ExpenseCategory.TOLL, amount = 2400.0, date = now - day, location = "Delhi-Jaipur Highway"),
            Expense(tripId = "t2", category = ExpenseCategory.REPAIR, amount = 3500.0, date = now - day, note = "Tyre puncture repair"),
            Expense(tripId = "t2", category = ExpenseCategory.FOOD, amount = 500.0, date = now - day, location = "Neemrana Dhaba"),
            Expense(tripId = "t3", category = ExpenseCategory.DIESEL, amount = 6000.0, date = now - 5 * 3600000, location = "Indian Oil, Ahmedabad"),
            Expense(tripId = "t3", category = ExpenseCategory.TOLL, amount = 800.0, date = now - 4 * 3600000),
            Expense(tripId = "t4", category = ExpenseCategory.DIESEL, amount = 9500.0, date = now - 10 * 3600000, location = "BPCL, Chennai"),
            Expense(tripId = "t4", category = ExpenseCategory.TOLL, amount = 1800.0, date = now - 9 * 3600000),
            Expense(tripId = "t5", category = ExpenseCategory.DIESEL, amount = 5000.0, date = now - 3 * day),
            Expense(tripId = "t5", category = ExpenseCategory.TOLL, amount = 600.0, date = now - 3 * day),
            Expense(tripId = "t6", category = ExpenseCategory.DIESEL, amount = 15000.0, date = now - 5 * day),
            Expense(tripId = "t6", category = ExpenseCategory.TOLL, amount = 3200.0, date = now - 5 * day),
            Expense(tripId = "t6", category = ExpenseCategory.TYRE, amount = 8000.0, date = now - 5 * day, note = "Front tyre replaced"),
            Expense(category = ExpenseCategory.PARKING, amount = 200.0, date = now - 3 * 3600000, location = "Transport Nagar"),
            Expense(category = ExpenseCategory.REPAIR, amount = 2500.0, date = now - 2 * day, note = "Engine oil change")
        )

        _fuelEntries.value = listOf(
            FuelEntry(truckId = "truck1", date = now - 2 * day, odometer = 125000, liters = 120.0, pricePerLiter = 96.50, totalAmount = 11580.0, location = "Shell, Mumbai", kmPerLiter = 4.2),
            FuelEntry(truckId = "truck1", date = now - 5 * day, odometer = 124500, liters = 115.0, pricePerLiter = 96.20, totalAmount = 11063.0, location = "HP, Pune", kmPerLiter = 4.0),
            FuelEntry(truckId = "truck2", date = now - day, odometer = 98000, liters = 100.0, pricePerLiter = 96.50, totalAmount = 9650.0, location = "Indian Oil, Delhi", kmPerLiter = 3.8),
            FuelEntry(truckId = "truck2", date = now - 4 * day, odometer = 97600, liters = 110.0, pricePerLiter = 96.30, totalAmount = 10593.0, location = "BPCL, Jaipur", kmPerLiter = 3.6)
        )
    }
}
