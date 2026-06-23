package com.truckhisaab.ui.screens.truck

import androidx.lifecycle.ViewModel
import com.truckhisaab.data.AppContainer
import com.truckhisaab.data.model.Truck
import com.truckhisaab.data.model.TruckType
import com.truckhisaab.data.model.truckManufacturers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddTruckState(
    val number: String = "",
    val type: TruckType? = null,
    val manufacturer: String = "",
    val model: String = "",
    val year: String = "",
    val showSuccess: Boolean = false
)

class TruckViewModel : ViewModel() {
    private val repo = AppContainer.truckRepository
    val trucks = repo.trucks

    private val _addState = MutableStateFlow(AddTruckState())
    val addState: StateFlow<AddTruckState> = _addState.asStateFlow()

    fun updateField(field: String, value: String) {
        _addState.update {
            when (field) {
                "number" -> it.copy(number = value)
                "manufacturer" -> it.copy(manufacturer = value)
                "model" -> it.copy(model = value)
                "year" -> it.copy(year = value)
                else -> it
            }
        }
    }

    fun setType(t: TruckType) = _addState.update { it.copy(type = t) }

    fun saveTruck() {
        val s = _addState.value
        val t = s.type ?: return
        repo.addTruck(Truck(
            number = s.number, type = t, manufacturer = s.manufacturer,
            model = s.model, yearOfPurchase = s.year.toIntOrNull() ?: 2024
        ))
        _addState.value = AddTruckState(showSuccess = true)
    }

    fun resetAdd() { _addState.value = AddTruckState() }
    fun dismissSuccess() = _addState.update { it.copy(showSuccess = false) }
    fun getTruck(id: String) = repo.getTruck(id)
    fun deleteTruck(id: String) = repo.deleteTruck(id)
    fun getTruckTrips(number: String) = AppContainer.tripRepository.getTripsForTruck(number)
    fun getTruckExpenses(number: String) = AppContainer.expenseRepository.expenses.value.filter { it.truckId == number }
    fun getTruckDocs(number: String) = AppContainer.documentRepository.documents.value.filter { it.truckNumber == number }
}
