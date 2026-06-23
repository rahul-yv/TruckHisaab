package com.truckhisaab.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.truckhisaab.data.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileState(
    val name: String = "",
    val phone: String = "",
    val role: String = "",
    val totalTrips: Int = 0,
    val totalIncome: Double = 0.0,
    val totalTrucks: Int = 0,
    val totalDrivers: Int = 0
)

class ProfileViewModel : ViewModel() {
    private val userRepo = AppContainer.userRepository
    private val tripRepo = AppContainer.tripRepository
    private val truckRepo = AppContainer.truckRepository
    private val driverRepo = AppContainer.driverRepository

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init { refresh() }

    fun refresh() {
        val user = userRepo.user.value
        _state.value = ProfileState(
            name = user?.name ?: "User",
            phone = user?.phone ?: "",
            role = user?.role?.hindiLabel ?: "",
            totalTrips = tripRepo.trips.value.size,
            totalIncome = tripRepo.trips.value.sumOf { it.freightAmount },
            totalTrucks = truckRepo.trucks.value.size,
            totalDrivers = driverRepo.drivers.value.size
        )
    }
}
