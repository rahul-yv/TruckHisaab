package com.truckhisaab.data.repository

import com.truckhisaab.data.local.dao.DriverDao
import com.truckhisaab.data.mapper.toDomain
import com.truckhisaab.data.mapper.toEntity
import com.truckhisaab.domain.model.Driver
import com.truckhisaab.domain.repository.DriverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverRepositoryImpl @Inject constructor(
    private val driverDao: DriverDao
) : DriverRepository {

    override fun getAllDrivers(): Flow<List<Driver>> =
        driverDao.getAllDrivers().map { list -> list.map { it.toDomain() } }

    override fun getDriverCount(): Flow<Int> = driverDao.getDriverCount()

    override suspend fun getDriverById(id: Long): Driver? =
        driverDao.getDriverById(id)?.toDomain()

    override suspend fun addDriver(driver: Driver): Long =
        driverDao.insert(driver.toEntity())

    override suspend fun updateDriver(driver: Driver) =
        driverDao.update(driver.toEntity())

    override suspend fun deleteDriver(driver: Driver) =
        driverDao.delete(driver.toEntity())
}
