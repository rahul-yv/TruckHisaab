package com.truckhisaab.ui.screens.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.*
import com.truckhisaab.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddDriverState(
    val name: String = "", val phone: String = "",
    val license: String = "", val salary: String = "",
    val isSaving: Boolean = false
)

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val driverRepo: DriverRepository,
    private val tripRepo: TripRepository
) : ViewModel() {

    val drivers = driverRepo.getAllDrivers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _addState = MutableStateFlow(AddDriverState())
    val addState: StateFlow<AddDriverState> = _addState.asStateFlow()

    fun updateAddState(update: AddDriverState.() -> AddDriverState) { _addState.update { it.update() } }
    fun resetAddState() { _addState.value = AddDriverState() }

    fun saveDriver(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _addState.update { it.copy(isSaving = true) }
            val s = _addState.value
            driverRepo.addDriver(Driver(
                name = s.name, phone = s.phone, licenseNumber = s.license,
                monthlySalary = s.salary.toDoubleOrNull() ?: 0.0
            ))
            _addState.update { it.copy(isSaving = false) }
            resetAddState()
            onSuccess()
        }
    }

    fun deleteDriver(id: Long, onDone: () -> Unit) {
        viewModelScope.launch {
            driverRepo.getDriverById(id)?.let { driverRepo.deleteDriver(it) }
            onDone()
        }
    }

    fun getTripsForDriver(id: Long) = tripRepo.getTripsForDriver(id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
