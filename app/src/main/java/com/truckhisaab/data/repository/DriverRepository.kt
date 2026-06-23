package com.truckhisaab.data.repository

import com.truckhisaab.data.model.Driver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DriverRepository {
    private val _drivers = MutableStateFlow<List<Driver>>(emptyList())
    val drivers: StateFlow<List<Driver>> = _drivers.asStateFlow()

    init { seedData() }

    fun addDriver(driver: Driver) = _drivers.update { it + driver }
    fun updateDriver(driver: Driver) = _drivers.update { list -> list.map { if (it.id == driver.id) driver else it } }
    fun deleteDriver(id: String) = _drivers.update { it.filter { d -> d.id != id } }
    fun getDriver(id: String): Driver? = _drivers.value.find { it.id == id }

    private fun seedData() {
        val day = 86400000L
        val now = System.currentTimeMillis()
        _drivers.value = listOf(
            Driver(id = "drv1", name = "Ramesh Kumar", phone = "+91 98765 43210", licenseNumber = "HR/DL/2020/9876", licenseExpiry = now + 200 * day, monthlySalary = 25000.0, advanceTaken = 5000.0, isActive = true, totalTrips = 45),
            Driver(id = "drv2", name = "Suresh Yadav", phone = "+91 98765 43211", licenseNumber = "DL/HR/2019/5432", licenseExpiry = now + 120 * day, monthlySalary = 22000.0, advanceTaken = 3000.0, isActive = true, totalTrips = 38),
            Driver(id = "drv3", name = "Mohan Singh", phone = "+91 98765 43212", licenseNumber = "GJ/DL/2021/7654", licenseExpiry = now + 350 * day, monthlySalary = 20000.0, isActive = true, totalTrips = 28),
            Driver(id = "drv4", name = "Vijay Sharma", phone = "+91 98765 43213", licenseNumber = "TN/DL/2018/3210", licenseExpiry = now + 50 * day, monthlySalary = 23000.0, advanceTaken = 8000.0, isActive = true, totalTrips = 52),
            Driver(id = "drv5", name = "Anil Verma", phone = "+91 98765 43214", licenseNumber = "UP/DL/2020/1234", licenseExpiry = now - 10 * day, monthlySalary = 18000.0, isActive = false, totalTrips = 15)
        )
    }
}
