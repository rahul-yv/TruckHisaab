package com.truckhisaab.domain.repository

import com.truckhisaab.domain.model.Truck
import kotlinx.coroutines.flow.Flow

interface TruckRepository {
    fun getAllTrucks(): Flow<List<Truck>>
    fun getTruckCount(): Flow<Int>
    suspend fun getTruckById(id: Long): Truck?
    suspend fun addTruck(truck: Truck): Long
    suspend fun updateTruck(truck: Truck)
    suspend fun deleteTruck(truck: Truck)
}
