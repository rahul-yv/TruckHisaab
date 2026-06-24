package com.truckhisaab.domain.repository

import com.truckhisaab.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getAllTrips(): Flow<List<Trip>>
    fun getTripsByStatus(status: String): Flow<List<Trip>>
    fun getTripsForTruck(truckId: Long): Flow<List<Trip>>
    fun getTripsForDriver(driverId: Long): Flow<List<Trip>>
    fun getActiveTrips(): Flow<List<Trip>>
    fun getIncomeSince(since: Long): Flow<Double>
    fun getTripCount(): Flow<Int>
    suspend fun getTripById(id: Long): Trip?
    suspend fun addTrip(trip: Trip): Long
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(id: Long)
}
