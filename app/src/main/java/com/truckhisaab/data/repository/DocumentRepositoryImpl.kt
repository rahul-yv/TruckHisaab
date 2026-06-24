package com.truckhisaab.data.repository

import com.truckhisaab.data.local.dao.DocumentDao
import com.truckhisaab.data.mapper.toDomain
import com.truckhisaab.data.mapper.toEntity
import com.truckhisaab.domain.model.Document
import com.truckhisaab.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepositoryImpl @Inject constructor(
    private val documentDao: DocumentDao
) : DocumentRepository {

    override fun getAllDocuments(): Flow<List<Document>> =
        documentDao.getAllDocuments().map { list -> list.map { it.toDomain() } }

    override fun getDocumentsForTruck(truckId: Long): Flow<List<Document>> =
        documentDao.getDocumentsForTruck(truckId).map { list -> list.map { it.toDomain() } }

    override fun getExpiringDocuments(withinDays: Int): Flow<List<Document>> {
        val threshold = System.currentTimeMillis() + (withinDays.toLong() * 24 * 60 * 60 * 1000)
        return documentDao.getExpiringDocuments(threshold).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getDocumentById(id: Long): Document? =
        documentDao.getDocumentById(id)?.toDomain()

    override suspend fun addDocument(document: Document): Long =
        documentDao.insert(document.toEntity())

    override suspend fun updateDocument(document: Document) =
        documentDao.update(document.toEntity())

    override suspend fun deleteDocument(id: Long) =
        documentDao.deleteById(id)
}
