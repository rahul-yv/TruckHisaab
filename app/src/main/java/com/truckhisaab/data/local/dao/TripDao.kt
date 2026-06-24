package com.truckhisaab.data.local.dao

import androidx.room.*
import com.truckhisaab.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: Long): TripEntity?

    @Query("SELECT * FROM trips WHERE status = :status ORDER BY createdAt DESC")
    fun getTripsByStatus(status: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE truckId = :truckId ORDER BY createdAt DESC")
    fun getTripsForTruck(truckId: Long): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE driverId = :driverId ORDER BY createdAt DESC")
    fun getTripsForDriver(driverId: Long): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE status = 'ACTIVE'")
    fun getActiveTrips(): Flow<List<TripEntity>>

    @Query("SELECT COALESCE(SUM(freightAmount), 0.0) FROM trips WHERE date(startDate/1000, 'unixepoch', 'localtime') = date('now', 'localtime') AND status != 'CANCELLED'")
    fun getTodayIncome(): Flow<Double>

    @Query("SELECT COALESCE(SUM(freightAmount), 0.0) FROM trips WHERE startDate >= :since AND status != 'CANCELLED'")
    fun getIncomeSince(since: Long): Flow<Double>

    @Query("SELECT COUNT(*) FROM trips")
    fun getTripCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: TripEntity): Long

    @Update
    suspend fun update(trip: TripEntity)

    @Delete
    suspend fun delete(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteById(id: Long)
}
