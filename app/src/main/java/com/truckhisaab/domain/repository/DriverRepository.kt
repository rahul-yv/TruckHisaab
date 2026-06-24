package com.truckhisaab.domain.repository

import com.truckhisaab.domain.model.Driver
import kotlinx.coroutines.flow.Flow

interface DriverRepository {
    fun getAllDrivers(): Flow<List<Driver>>
    fun getDriverCount(): Flow<Int>
    suspend fun getDriverById(id: Long): Driver?
    suspend fun addDriver(driver: Driver): Long
    suspend fun updateDriver(driver: Driver)
    suspend fun deleteDriver(driver: Driver)
}
