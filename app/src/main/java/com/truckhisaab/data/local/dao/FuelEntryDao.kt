package com.truckhisaab.data.local.dao

import androidx.room.*
import com.truckhisaab.data.local.entity.FuelEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelEntryDao {
    @Query("SELECT * FROM fuel_entries ORDER BY date DESC")
    fun getAllFuelEntries(): Flow<List<FuelEntryEntity>>

    @Query("SELECT * FROM fuel_entries WHERE truckId = :truckId ORDER BY date DESC")
    fun getFuelEntriesForTruck(truckId: Long): Flow<List<FuelEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fuelEntry: FuelEntryEntity): Long

    @Delete
    suspend fun delete(fuelEntry: FuelEntryEntity)
}
