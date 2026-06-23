package com.truckhisaab.ui.screens.driver

import androidx.lifecycle.ViewModel
import com.truckhisaab.data.AppContainer
import com.truckhisaab.data.model.Driver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddDriverState(
    val name: String = "",
    val phone: String = "",
    val licenseNumber: String = "",
    val salary: String = "",
    val showSuccess: Boolean = false
)

class DriverViewModel : ViewModel() {
    private val repo = AppContainer.driverRepository
    val drivers = repo.drivers

    private val _addState = MutableStateFlow(AddDriverState())
    val addState: StateFlow<AddDriverState> = _addState.asStateFlow()

    fun updateField(field: String, value: String) {
        _addState.update {
            when (field) {
                "name" -> it.copy(name = value)
                "phone" -> it.copy(phone = value)
                "license" -> it.copy(licenseNumber = value)
                "salary" -> it.copy(salary = value)
                else -> it
            }
        }
    }

    fun saveDriver() {
        val s = _addState.value
        if (s.name.isBlank()) return
        repo.addDriver(Driver(
            name = s.name, phone = s.phone,
            licenseNumber = s.licenseNumber,
            monthlySalary = s.salary.toDoubleOrNull() ?: 0.0
        ))
        _addState.value = AddDriverState(showSuccess = true)
    }

    fun resetAdd() { _addState.value = AddDriverState() }
    fun dismissSuccess() = _addState.update { it.copy(showSuccess = false) }
    fun getDriver(id: String) = repo.getDriver(id)
    fun deleteDriver(id: String) = repo.deleteDriver(id)
    fun getDriverTrips(name: String) = AppContainer.tripRepository.trips.value.filter { it.driverName == name }
}
