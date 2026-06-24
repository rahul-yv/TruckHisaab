package com.truckhisaab.domain.repository

import com.truckhisaab.domain.model.Document
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    fun getAllDocuments(): Flow<List<Document>>
    fun getDocumentsForTruck(truckId: Long): Flow<List<Document>>
    fun getExpiringDocuments(withinDays: Int): Flow<List<Document>>
    suspend fun getDocumentById(id: Long): Document?
    suspend fun addDocument(document: Document): Long
    suspend fun updateDocument(document: Document)
    suspend fun deleteDocument(id: Long)
}
