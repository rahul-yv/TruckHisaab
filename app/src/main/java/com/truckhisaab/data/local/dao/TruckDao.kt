package com.truckhisaab.data.local.dao

import androidx.room.*
import com.truckhisaab.data.local.entity.TruckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {
    @Query("SELECT * FROM trucks WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllTrucks(): Flow<List<TruckEntity>>

    @Query("SELECT * FROM trucks WHERE id = :id")
    suspend fun getTruckById(id: Long): TruckEntity?

    @Query("SELECT COUNT(*) FROM trucks WHERE isActive = 1")
    fun getTruckCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(truck: TruckEntity): Long

    @Update
    suspend fun update(truck: TruckEntity)

    @Delete
    suspend fun delete(truck: TruckEntity)
}
