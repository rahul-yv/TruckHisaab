package com.truckhisaab.domain.usecase

import com.truckhisaab.domain.model.Driver
import com.truckhisaab.domain.repository.DriverRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDriversUseCase @Inject constructor(private val repo: DriverRepository) {
    operator fun invoke(): Flow<List<Driver>> = repo.getAllDrivers()
    fun count(): Flow<Int> = repo.getDriverCount()
}

class AddDriverUseCase @Inject constructor(private val repo: DriverRepository) {
    suspend operator fun invoke(driver: Driver): Long = repo.addDriver(driver)
}

class UpdateDriverUseCase @Inject constructor(private val repo: DriverRepository) {
    suspend operator fun invoke(driver: Driver) = repo.updateDriver(driver)
}

class DeleteDriverUseCase @Inject constructor(private val repo: DriverRepository) {
    suspend operator fun invoke(driver: Driver) = repo.deleteDriver(driver)
}
