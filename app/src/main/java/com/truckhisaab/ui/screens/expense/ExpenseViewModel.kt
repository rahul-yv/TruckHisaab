package com.truckhisaab.ui.screens.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.*
import com.truckhisaab.domain.repository.*
import com.truckhisaab.util.startOfMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddExpenseState(
    val category: ExpenseCategory? = null, val amount: String = "",
    val note: String = "", val truckId: Long = 0, val truckNumber: String = "",
    val tripId: Long? = null, val liters: String = "", val pricePerLiter: String = "",
    val isSaving: Boolean = false
)

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepo: ExpenseRepository,
    private val truckRepo: TruckRepository
) : ViewModel() {

    val expenses = expenseRepo.getAllExpenses().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val trucks = truckRepo.getAllTrucks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val fuelEntries = expenseRepo.getAllFuelEntries().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val categoryBreakdown = expenseRepo.getCategoryBreakdown(startOfMonth()).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _addState = MutableStateFlow(AddExpenseState())
    val addState: StateFlow<AddExpenseState> = _addState.asStateFlow()

    private val _filterCategory = MutableStateFlow<ExpenseCategory?>(null)
    val filterCategory: StateFlow<ExpenseCategory?> = _filterCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredExpenses = combine(expenses, _filterCategory, _searchQuery) { list, cat, q ->
        list.filter { e ->
            val matchCat = cat == null || e.category == cat
            val matchQ = q.isBlank() || e.note.contains(q, true) || e.truckNumber.contains(q, true)
            matchCat && matchQ
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateFilter(c: ExpenseCategory?) { _filterCategory.value = c }
    fun updateSearch(q: String) { _searchQuery.value = q }
    fun updateAddState(update: AddExpenseState.() -> AddExpenseState) { _addState.update { it.update() } }
    fun resetAddState() { _addState.value = AddExpenseState() }

    fun saveExpense(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _addState.update { it.copy(isSaving = true) }
            val s = _addState.value
            val amt = s.amount.toDoubleOrNull() ?: 0.0
            expenseRepo.addExpense(Expense(
                category = s.category ?: ExpenseCategory.OTHER, amount = amt,
                note = s.note, truckId = s.truckId, truckNumber = s.truckNumber, tripId = s.tripId
            ))
            if (s.category == ExpenseCategory.DIESEL && s.liters.isNotBlank()) {
                expenseRepo.addFuelEntry(FuelEntry(
                    truckId = s.truckId, truckNumber = s.truckNumber,
                    liters = s.liters.toDoubleOrNull() ?: 0.0,
                    pricePerLiter = s.pricePerLiter.toDoubleOrNull() ?: 0.0,
                    totalAmount = amt
                ))
            }
            _addState.update { it.copy(isSaving = false) }
            resetAddState()
            onSuccess()
        }
    }

    fun deleteExpense(id: Long) { viewModelScope.launch { expenseRepo.deleteExpense(id) } }
}
