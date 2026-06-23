package com.truckhisaab.data.repository

import com.truckhisaab.data.model.Document
import com.truckhisaab.data.model.DocumentType
import com.truckhisaab.data.model.RenewalEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DocumentRepository {
    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents: StateFlow<List<Document>> = _documents.asStateFlow()

    init { seedData() }

    fun addDocument(doc: Document) = _documents.update { it + doc }
    fun updateDocument(doc: Document) = _documents.update { list -> list.map { if (it.id == doc.id) doc else it } }
    fun deleteDocument(id: String) = _documents.update { it.filter { d -> d.id != id } }
    fun getDocument(id: String): Document? = _documents.value.find { it.id == id }
    fun getDocumentsForTruck(truckId: String): List<Document> = _documents.value.filter { it.truckId == truckId }

    fun getExpiringDocuments(withinDays: Int = 30): List<Document> {
        val cutoff = System.currentTimeMillis() + withinDays * 86400000L
        return _documents.value.filter { it.expiryDate <= cutoff }
    }

    private fun seedData() {
        val day = 86400000L
        val now = System.currentTimeMillis()
        _documents.value = listOf(
            Document(id = "d1", type = DocumentType.RC, truckId = "truck1", truckNumber = "MH 12 AB 1234", documentNumber = "MH12/2021/RC/7891", issueDate = now - 365 * day, expiryDate = now + 120 * day, renewalHistory = listOf(RenewalEntry(now - 365 * day, "New registration"))),
            Document(id = "d2", type = DocumentType.INSURANCE, truckId = "truck1", truckNumber = "MH 12 AB 1234", documentNumber = "INS/2025/456789", issueDate = now - 340 * day, expiryDate = now + 22 * day),
            Document(id = "d3", type = DocumentType.PUC, truckId = "truck1", truckNumber = "MH 12 AB 1234", documentNumber = "PUC/MH/2025/1234", issueDate = now - 170 * day, expiryDate = now + 8 * day),
            Document(id = "d4", type = DocumentType.PERMIT, truckId = "truck2", truckNumber = "HR 26 CD 5678", documentNumber = "NP/2024/5678", issueDate = now - 400 * day, expiryDate = now - 5 * day),
            Document(id = "d5", type = DocumentType.LICENSE, truckNumber = "Driver - Ramesh", documentNumber = "DL/HR/2020/9876", issueDate = now - 600 * day, expiryDate = now + 200 * day),
            Document(id = "d6", type = DocumentType.INSURANCE, truckId = "truck2", truckNumber = "HR 26 CD 5678", documentNumber = "INS/2025/987654", issueDate = now - 320 * day, expiryDate = now + 45 * day),
            Document(id = "d7", type = DocumentType.PUC, truckId = "truck2", truckNumber = "HR 26 CD 5678", documentNumber = "PUC/HR/2025/5678", issueDate = now - 150 * day, expiryDate = now + 60 * day),
            Document(id = "d8", type = DocumentType.FITNESS, truckId = "truck1", truckNumber = "MH 12 AB 1234", documentNumber = "FIT/MH/2025/3456", issueDate = now - 200 * day, expiryDate = now + 165 * day),
            Document(id = "d9", type = DocumentType.RC, truckId = "truck3", truckNumber = "GJ 01 EF 9012", documentNumber = "GJ01/2022/RC/4567", issueDate = now - 500 * day, expiryDate = now + 230 * day)
        )
    }
}
