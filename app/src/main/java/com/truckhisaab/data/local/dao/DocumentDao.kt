package com.truckhisaab.data.local.dao

import androidx.room.*
import com.truckhisaab.data.local.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY expiryDate ASC")
    fun getAllDocuments(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE id = :id")
    suspend fun getDocumentById(id: Long): DocumentEntity?

    @Query("SELECT * FROM documents WHERE truckId = :truckId ORDER BY expiryDate ASC")
    fun getDocumentsForTruck(truckId: Long): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE expiryDate <= :threshold AND expiryDate > 0 ORDER BY expiryDate ASC")
    fun getExpiringDocuments(threshold: Long): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE expiryDate <= :threshold AND expiryDate > 0 ORDER BY expiryDate ASC")
    suspend fun getExpiringDocumentsSync(threshold: Long): List<DocumentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: DocumentEntity): Long

    @Update
    suspend fun update(document: DocumentEntity)

    @Delete
    suspend fun delete(document: DocumentEntity)

    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun deleteById(id: Long)
}
