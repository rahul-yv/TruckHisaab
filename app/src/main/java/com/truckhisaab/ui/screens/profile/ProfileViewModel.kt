package com.truckhisaab.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.User
import com.truckhisaab.domain.repository.*
import com.truckhisaab.data.security.SecurePrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val user: User? = null, val truckCount: Int = 0,
    val driverCount: Int = 0, val tripCount: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val truckRepo: TruckRepository,
    private val driverRepo: DriverRepository,
    private val tripRepo: TripRepository,
    private val securePrefs: SecurePrefs
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        userRepo.getUser().onEach { u -> _state.update { it.copy(user = u) } }.launchIn(viewModelScope)
        truckRepo.getTruckCount().onEach { c -> _state.update { it.copy(truckCount = c) } }.launchIn(viewModelScope)
        driverRepo.getDriverCount().onEach { c -> _state.update { it.copy(driverCount = c) } }.launchIn(viewModelScope)
        tripRepo.getTripCount().onEach { c -> _state.update { it.copy(tripCount = c) } }.launchIn(viewModelScope)
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            userRepo.updateLogin(false)
            securePrefs.clear()
            onDone()
        }
    }

    fun deleteAccount(onDone: () -> Unit) {
        viewModelScope.launch {
            userRepo.deleteUser()
            securePrefs.clear()
            onDone()
        }
    }
}
