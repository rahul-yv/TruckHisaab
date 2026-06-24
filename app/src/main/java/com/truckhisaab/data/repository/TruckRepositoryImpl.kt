package com.truckhisaab.data.repository

import com.truckhisaab.data.local.dao.TruckDao
import com.truckhisaab.data.mapper.toDomain
import com.truckhisaab.data.mapper.toEntity
import com.truckhisaab.domain.model.Truck
import com.truckhisaab.domain.repository.TruckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TruckRepositoryImpl @Inject constructor(
    private val truckDao: TruckDao
) : TruckRepository {

    override fun getAllTrucks(): Flow<List<Truck>> =
        truckDao.getAllTrucks().map { list -> list.map { it.toDomain() } }

    override fun getTruckCount(): Flow<Int> = truckDao.getTruckCount()

    override suspend fun getTruckById(id: Long): Truck? =
        truckDao.getTruckById(id)?.toDomain()

    override suspend fun addTruck(truck: Truck): Long =
        truckDao.insert(truck.toEntity())

    override suspend fun updateTruck(truck: Truck) =
        truckDao.update(truck.toEntity())

    override suspend fun deleteTruck(truck: Truck) =
        truckDao.delete(truck.toEntity())
}
