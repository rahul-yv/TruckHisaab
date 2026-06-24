package com.truckhisaab.ui.screens.truck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.*
import com.truckhisaab.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTruckState(
    val number: String = "", val type: TruckType = TruckType.OPEN_BODY,
    val manufacturer: String = "", val model: String = "",
    val year: String = "2024", val isSaving: Boolean = false
)

@HiltViewModel
class TruckViewModel @Inject constructor(
    private val truckRepo: TruckRepository,
    private val tripRepo: TripRepository,
    private val expenseRepo: ExpenseRepository,
    private val documentRepo: DocumentRepository
) : ViewModel() {

    val trucks = truckRepo.getAllTrucks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _addState = MutableStateFlow(AddTruckState())
    val addState: StateFlow<AddTruckState> = _addState.asStateFlow()

    fun updateAddState(update: AddTruckState.() -> AddTruckState) { _addState.update { it.update() } }
    fun resetAddState() { _addState.value = AddTruckState() }

    fun saveTruck(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _addState.update { it.copy(isSaving = true) }
            val s = _addState.value
            truckRepo.addTruck(Truck(
                number = s.number.uppercase(), type = s.type, manufacturer = s.manufacturer,
                model = s.model, yearOfPurchase = s.year.toIntOrNull() ?: 2024
            ))
            _addState.update { it.copy(isSaving = false) }
            resetAddState()
            onSuccess()
        }
    }

    fun getTripsForTruck(id: Long) = tripRepo.getTripsForTruck(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun getExpensesForTruck(id: Long) = expenseRepo.getExpensesForTruck(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun getDocsForTruck(id: Long) = documentRepo.getDocumentsForTruck(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
