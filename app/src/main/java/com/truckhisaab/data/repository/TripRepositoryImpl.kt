package com.truckhisaab.data.repository

import com.truckhisaab.data.local.dao.TripDao
import com.truckhisaab.data.mapper.toDomain
import com.truckhisaab.data.mapper.toEntity
import com.truckhisaab.domain.model.Trip
import com.truckhisaab.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao
) : TripRepository {

    override fun getAllTrips(): Flow<List<Trip>> =
        tripDao.getAllTrips().map { list -> list.map { it.toDomain() } }

    override fun getTripsByStatus(status: String): Flow<List<Trip>> =
        tripDao.getTripsByStatus(status).map { list -> list.map { it.toDomain() } }

    override fun getTripsForTruck(truckId: Long): Flow<List<Trip>> =
        tripDao.getTripsForTruck(truckId).map { list -> list.map { it.toDomain() } }

    override fun getTripsForDriver(driverId: Long): Flow<List<Trip>> =
        tripDao.getTripsForDriver(driverId).map { list -> list.map { it.toDomain() } }

    override fun getActiveTrips(): Flow<List<Trip>> =
        tripDao.getActiveTrips().map { list -> list.map { it.toDomain() } }

    override fun getIncomeSince(since: Long): Flow<Double> =
        tripDao.getIncomeSince(since)

    override fun getTripCount(): Flow<Int> = tripDao.getTripCount()

    override suspend fun getTripById(id: Long): Trip? =
        tripDao.getTripById(id)?.toDomain()

    override suspend fun addTrip(trip: Trip): Long =
        tripDao.insert(trip.toEntity())

    override suspend fun updateTrip(trip: Trip) =
        tripDao.update(trip.toEntity())

    override suspend fun deleteTrip(id: Long) =
        tripDao.deleteById(id)
}
