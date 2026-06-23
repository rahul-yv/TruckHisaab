package com.truckhisaab.ui.screens.expense

import androidx.lifecycle.ViewModel
import com.truckhisaab.data.AppContainer
import com.truckhisaab.data.model.Expense
import com.truckhisaab.data.model.ExpenseCategory
import com.truckhisaab.data.model.FuelEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddExpenseState(
    val category: ExpenseCategory? = null,
    val amount: String = "",
    val description: String = "",
    val tripId: String? = null,
    val truckNumber: String = "",
    val liters: String = "",
    val ratePerLiter: String = "",
    val showSuccess: Boolean = false
)

data class ExpenseListFilter(
    val category: ExpenseCategory? = null,
    val searchQuery: String = ""
)

class ExpenseViewModel : ViewModel() {
    private val repo = AppContainer.expenseRepository
    val expenses = repo.expenses
    val fuelEntries = repo.fuelEntries

    private val _addState = MutableStateFlow(AddExpenseState())
    val addState: StateFlow<AddExpenseState> = _addState.asStateFlow()

    private val _filter = MutableStateFlow(ExpenseListFilter())
    val filter: StateFlow<ExpenseListFilter> = _filter.asStateFlow()

    fun filteredExpenses(): List<Expense> {
        val f = _filter.value
        var list = expenses.value
        if (f.category != null) list = list.filter { it.category == f.category }
        if (f.searchQuery.isNotBlank()) {
            val q = f.searchQuery.lowercase()
            list = list.filter { it.note.lowercase().contains(q) || it.category.label.lowercase().contains(q) }
        }
        return list.sortedByDescending { it.date }
    }

    fun setFilterCategory(c: ExpenseCategory?) = _filter.update { it.copy(category = c) }
    fun setSearch(q: String) = _filter.update { it.copy(searchQuery = q) }

    fun updateField(field: String, value: String) {
        _addState.update {
            when (field) {
                "amount" -> it.copy(amount = value)
                "desc" -> it.copy(description = value)
                "truck" -> it.copy(truckNumber = value)
                "liters" -> it.copy(liters = value)
                "rate" -> it.copy(ratePerLiter = value)
                else -> it
            }
        }
    }

    fun setCategory(c: ExpenseCategory) = _addState.update { it.copy(category = c) }
    fun setTripId(id: String?) = _addState.update { it.copy(tripId = id) }

    fun saveExpense() {
        val s = _addState.value
        val cat = s.category ?: return
        val amt = s.amount.toDoubleOrNull() ?: return
        repo.addExpense(Expense(
            category = cat, amount = amt, note = s.description,
            tripId = s.tripId, truckId = s.truckNumber
        ))
        if (cat == ExpenseCategory.DIESEL && s.liters.isNotBlank()) {
            repo.addFuelEntry(FuelEntry(
                truckId = s.truckNumber,
                liters = s.liters.toDoubleOrNull() ?: 0.0,
                pricePerLiter = s.ratePerLiter.toDoubleOrNull() ?: 0.0,
                totalAmount = amt
            ))
        }
        _addState.value = AddExpenseState(showSuccess = true)
    }

    fun resetAdd() { _addState.value = AddExpenseState() }
    fun dismissSuccess() = _addState.update { it.copy(showSuccess = false) }

    fun getCategoryBreakdown() = repo.getCategoryBreakdown()
    fun getTodayExpense() = repo.getTodayExpense()
    fun getWeekExpense() = repo.getWeekExpense()
    fun getMonthExpense() = repo.getMonthExpense()
    fun deleteExpense(id: String) = repo.deleteExpense(id)
}
