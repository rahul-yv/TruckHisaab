package com.truckhisaab.data.repository

import com.truckhisaab.data.model.Truck
import com.truckhisaab.data.model.TruckType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TruckRepository {
    private val _trucks = MutableStateFlow<List<Truck>>(emptyList())
    val trucks: StateFlow<List<Truck>> = _trucks.asStateFlow()

    init { seedData() }

    fun addTruck(truck: Truck) = _trucks.update { it + truck }
    fun updateTruck(truck: Truck) = _trucks.update { list -> list.map { if (it.id == truck.id) truck else it } }
    fun deleteTruck(id: String) = _trucks.update { it.filter { t -> t.id != id } }
    fun getTruck(id: String): Truck? = _trucks.value.find { it.id == id }

    private fun seedData() {
        _trucks.value = listOf(
            Truck(id = "truck1", number = "MH 12 AB 1234", type = TruckType.HCV, manufacturer = "Tata", model = "Prima 4928.S", yearOfPurchase = 2021, currentOdometer = 125000, isActive = true),
            Truck(id = "truck2", number = "HR 26 CD 5678", type = TruckType.HCV, manufacturer = "Ashok Leyland", model = "Captain 4923", yearOfPurchase = 2019, currentOdometer = 98000, isActive = true),
            Truck(id = "truck3", number = "GJ 01 EF 9012", type = TruckType.LCV, manufacturer = "BharatBenz", model = "1917R", yearOfPurchase = 2022, currentOdometer = 65000, isActive = true)
        )
    }
}
