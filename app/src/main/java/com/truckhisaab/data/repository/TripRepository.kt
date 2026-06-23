package com.truckhisaab.data.repository

import com.truckhisaab.data.model.Trip
import com.truckhisaab.data.model.TripStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TripRepository {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    init { seedData() }

    fun getTrip(id: String): Trip? = _trips.value.find { it.id == id }

    fun addTrip(trip: Trip) = _trips.update { it + trip }

    fun updateTrip(trip: Trip) = _trips.update { list ->
        list.map { if (it.id == trip.id) trip else it }
    }

    fun deleteTrip(id: String) = _trips.update { list -> list.filter { it.id != id } }

    fun completeTrip(id: String) = _trips.update { list ->
        list.map {
            if (it.id == id) it.copy(status = TripStatus.COMPLETED, endDate = System.currentTimeMillis())
            else it
        }
    }

    fun getActiveTrips(): List<Trip> = _trips.value.filter { it.status == TripStatus.ACTIVE }
    fun getCompletedTrips(): List<Trip> = _trips.value.filter { it.status == TripStatus.COMPLETED }
    fun getTripsForTruck(truckId: String): List<Trip> = _trips.value.filter { it.truckId == truckId }
    fun getTripsForDriver(driverId: String): List<Trip> = _trips.value.filter { it.driverId == driverId }

    fun getTotalIncome(): Double = _trips.value.sumOf { it.freightAmount }
    fun getTodayIncome(): Double {
        val todayStart = todayStartMillis()
        return _trips.value.filter { it.startDate >= todayStart }.sumOf { it.freightAmount }
    }
    fun getWeekIncome(): Double {
        val weekStart = System.currentTimeMillis() - 7 * 86400000L
        return _trips.value.filter { it.startDate >= weekStart }.sumOf { it.freightAmount }
    }
    fun getMonthIncome(): Double {
        val monthStart = System.currentTimeMillis() - 30 * 86400000L
        return _trips.value.filter { it.startDate >= monthStart }.sumOf { it.freightAmount }
    }

    private fun todayStartMillis(): Long {
        val now = System.currentTimeMillis()
        return now - (now % 86400000L)
    }

    private fun seedData() {
        val day = 86400000L
        val now = System.currentTimeMillis()
        _trips.value = listOf(
            Trip(id = "t1", fromLocation = "Mumbai", toLocation = "Pune", cargoType = "Cement", weightTons = 20.0, freightAmount = 28000.0, advanceAmount = 5000.0, partyName = "Ambuja Cement", driverName = "Ramesh", truckNumber = "MH 12 AB 1234", startDate = now - 2 * day, endDate = now - day, status = TripStatus.COMPLETED),
            Trip(id = "t2", fromLocation = "Delhi", toLocation = "Jaipur", cargoType = "Steel", weightTons = 15.0, freightAmount = 45000.0, advanceAmount = 10000.0, partyName = "Tata Steel", driverName = "Suresh", truckNumber = "HR 26 CD 5678", startDate = now - day, endDate = now - 3600000, status = TripStatus.COMPLETED),
            Trip(id = "t3", fromLocation = "Ahmedabad", toLocation = "Surat", cargoType = "Grain", weightTons = 18.0, freightAmount = 22000.0, partyName = "Agro Traders", driverName = "Mohan", truckNumber = "GJ 01 EF 9012", startDate = now - 6 * 3600000, status = TripStatus.ACTIVE),
            Trip(id = "t4", fromLocation = "Chennai", toLocation = "Bangalore", cargoType = "Electronics", weightTons = 8.0, freightAmount = 35000.0, advanceAmount = 8000.0, partyName = "Samsung India", driverName = "Vijay", truckNumber = "TN 09 GH 3456", startDate = now - 12 * 3600000, status = TripStatus.ACTIVE),
            Trip(id = "t5", fromLocation = "Lucknow", toLocation = "Kanpur", cargoType = "Fertilizer", weightTons = 22.0, freightAmount = 18000.0, partyName = "IFFCO", driverName = "Anil", truckNumber = "UP 32 JK 7890", startDate = now - 3 * day, endDate = now - 2 * day, status = TripStatus.COMPLETED),
            Trip(id = "t6", fromLocation = "Kolkata", toLocation = "Bhubaneswar", cargoType = "Coal", weightTons = 25.0, freightAmount = 55000.0, advanceAmount = 15000.0, partyName = "Coal India", driverName = "Deepak", truckNumber = "MH 12 AB 1234", startDate = now - 5 * day, endDate = now - 4 * day, status = TripStatus.COMPLETED),
            Trip(id = "t7", fromLocation = "Hyderabad", toLocation = "Vijayawada", cargoType = "Cotton", weightTons = 12.0, freightAmount = 32000.0, partyName = "Textile Mills", driverName = "Raju", truckNumber = "HR 26 CD 5678", startDate = now - 4 * day, endDate = now - 3 * day, status = TripStatus.COMPLETED),
            Trip(id = "t8", fromLocation = "Indore", toLocation = "Bhopal", cargoType = "Sand", weightTons = 30.0, freightAmount = 15000.0, partyName = "Construction Co", driverName = "Ramesh", truckNumber = "GJ 01 EF 9012", startDate = now - 6 * day, status = TripStatus.CANCELLED)
        )
    }
}
