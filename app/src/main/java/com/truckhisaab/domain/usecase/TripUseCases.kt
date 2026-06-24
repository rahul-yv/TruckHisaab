package com.truckhisaab.domain.usecase

import com.truckhisaab.domain.model.Trip
import com.truckhisaab.domain.model.TripStatus
import com.truckhisaab.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTripsUseCase @Inject constructor(private val repo: TripRepository) {
    operator fun invoke(): Flow<List<Trip>> = repo.getAllTrips()
    fun byStatus(status: String): Flow<List<Trip>> = repo.getTripsByStatus(status)
    fun forTruck(truckId: Long): Flow<List<Trip>> = repo.getTripsForTruck(truckId)
    fun forDriver(driverId: Long): Flow<List<Trip>> = repo.getTripsForDriver(driverId)
}

class GetActiveTripUseCase @Inject constructor(private val repo: TripRepository) {
    operator fun invoke(): Flow<List<Trip>> = repo.getActiveTrips()
}

class CreateTripUseCase @Inject constructor(private val repo: TripRepository) {
    suspend operator fun invoke(trip: Trip): Long = repo.addTrip(trip)
}

class UpdateTripUseCase @Inject constructor(private val repo: TripRepository) {
    suspend operator fun invoke(trip: Trip) = repo.updateTrip(trip)
}

class CompleteTripUseCase @Inject constructor(private val repo: TripRepository) {
    suspend operator fun invoke(tripId: Long) {
        repo.getTripById(tripId)?.let { trip ->
            repo.updateTrip(trip.copy(status = TripStatus.COMPLETED, endDate = System.currentTimeMillis()))
        }
    }
}

class DeleteTripUseCase @Inject constructor(private val repo: TripRepository) {
    suspend operator fun invoke(tripId: Long) = repo.deleteTrip(tripId)
}
