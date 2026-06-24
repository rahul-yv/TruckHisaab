package com.truckhisaab.domain.usecase

import com.truckhisaab.domain.model.Document
import com.truckhisaab.domain.model.getDocumentStatus
import com.truckhisaab.domain.model.DocumentStatus
import com.truckhisaab.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDocumentsUseCase @Inject constructor(private val repo: DocumentRepository) {
    operator fun invoke(): Flow<List<Document>> = repo.getAllDocuments()
    fun forTruck(truckId: Long): Flow<List<Document>> = repo.getDocumentsForTruck(truckId)
}

class AddDocumentUseCase @Inject constructor(private val repo: DocumentRepository) {
    suspend operator fun invoke(document: Document): Long = repo.addDocument(document)
}

class DeleteDocumentUseCase @Inject constructor(private val repo: DocumentRepository) {
    suspend operator fun invoke(id: Long) = repo.deleteDocument(id)
}

class CheckDocumentExpiryUseCase @Inject constructor(private val repo: DocumentRepository) {
    fun getExpiring(withinDays: Int = 30): Flow<List<Document>> = repo.getExpiringDocuments(withinDays)

    fun getExpired(): Flow<List<Document>> = repo.getAllDocuments().map { docs ->
        docs.filter { getDocumentStatus(it.expiryDate) == DocumentStatus.EXPIRED }
    }
}
