package com.truckhisaab.ui.screens.documents

import androidx.lifecycle.ViewModel
import com.truckhisaab.data.AppContainer
import com.truckhisaab.data.model.Document
import com.truckhisaab.data.model.DocumentType
import com.truckhisaab.data.model.getDocumentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddDocState(
    val type: DocumentType? = null,
    val number: String = "",
    val truckNumber: String = "",
    val issueDate: Long = System.currentTimeMillis(),
    val expiryDate: Long = System.currentTimeMillis() + 365 * 86400000L,
    val reminderDays: Int = 30,
    val showSuccess: Boolean = false
)

class DocumentsViewModel : ViewModel() {
    private val repo = AppContainer.documentRepository
    val documents = repo.documents

    private val _addState = MutableStateFlow(AddDocState())
    val addState: StateFlow<AddDocState> = _addState.asStateFlow()

    private val _truckFilter = MutableStateFlow<String?>(null)
    val truckFilter: StateFlow<String?> = _truckFilter.asStateFlow()

    fun filteredDocs(): List<Document> {
        val tf = _truckFilter.value
        var list = documents.value
        if (tf != null) list = list.filter { it.truckNumber == tf }
        return list.sortedBy { it.expiryDate }
    }

    fun setTruckFilter(truck: String?) { _truckFilter.value = truck }

    fun updateAddField(field: String, value: String) {
        _addState.update {
            when (field) {
                "number" -> it.copy(number = value)
                "truck" -> it.copy(truckNumber = value)
                else -> it
            }
        }
    }

    fun setDocType(t: DocumentType) = _addState.update { it.copy(type = t) }
    fun setReminderDays(d: Int) = _addState.update { it.copy(reminderDays = d) }

    fun saveDoc() {
        val s = _addState.value
        val t = s.type ?: return
        repo.addDocument(Document(
            type = t, documentNumber = s.number, truckNumber = s.truckNumber,
            issueDate = s.issueDate, expiryDate = s.expiryDate,
            reminderDays = listOf(s.reminderDays, 15, 7, 1)
        ))
        _addState.value = AddDocState(showSuccess = true)
    }

    fun resetAdd() { _addState.value = AddDocState() }
    fun dismissSuccess() = _addState.update { it.copy(showSuccess = false) }
    fun deleteDoc(id: String) = repo.deleteDocument(id)
    fun getDoc(id: String) = repo.getDocument(id)
    fun getExpiringDocs() = repo.getExpiringDocuments(30)
}
