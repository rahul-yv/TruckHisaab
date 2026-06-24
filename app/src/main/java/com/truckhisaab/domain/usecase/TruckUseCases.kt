package com.truckhisaab.domain.usecase

import com.truckhisaab.domain.model.Truck
import com.truckhisaab.domain.repository.TruckRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrucksUseCase @Inject constructor(private val repo: TruckRepository) {
    operator fun invoke(): Flow<List<Truck>> = repo.getAllTrucks()
    fun count(): Flow<Int> = repo.getTruckCount()
}

class AddTruckUseCase @Inject constructor(private val repo: TruckRepository) {
    suspend operator fun invoke(truck: Truck): Long = repo.addTruck(truck)
}

class UpdateTruckUseCase @Inject constructor(private val repo: TruckRepository) {
    suspend operator fun invoke(truck: Truck) = repo.updateTruck(truck)
}

class DeleteTruckUseCase @Inject constructor(private val repo: TruckRepository) {
    suspend operator fun invoke(truck: Truck) = repo.deleteTruck(truck)
}
