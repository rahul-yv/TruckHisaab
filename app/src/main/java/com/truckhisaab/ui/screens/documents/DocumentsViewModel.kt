package com.truckhisaab.ui.screens.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truckhisaab.domain.model.*
import com.truckhisaab.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddDocState(
    val type: DocumentType? = null, val truckId: Long = 0, val truckNumber: String = "",
    val documentNumber: String = "", val issueDate: Long = System.currentTimeMillis(),
    val expiryDate: Long = System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000,
    val reminderDays: List<Int> = listOf(30, 15, 7), val isSaving: Boolean = false
)

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val documentRepo: DocumentRepository,
    private val truckRepo: TruckRepository
) : ViewModel() {

    val documents = documentRepo.getAllDocuments().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val trucks = truckRepo.getAllTrucks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _addState = MutableStateFlow(AddDocState())
    val addState: StateFlow<AddDocState> = _addState.asStateFlow()

    private val _truckFilter = MutableStateFlow(0L)
    val truckFilter: StateFlow<Long> = _truckFilter.asStateFlow()

    val filteredDocs = combine(documents, _truckFilter) { docs, tId ->
        if (tId == 0L) docs else docs.filter { it.truckId == tId }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setTruckFilter(id: Long) { _truckFilter.value = id }
    fun updateAddState(update: AddDocState.() -> AddDocState) { _addState.update { it.update() } }
    fun resetAddState() { _addState.value = AddDocState() }

    fun saveDocument(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _addState.update { it.copy(isSaving = true) }
            val s = _addState.value
            documentRepo.addDocument(Document(
                type = s.type ?: DocumentType.OTHER, truckId = s.truckId, truckNumber = s.truckNumber,
                documentNumber = s.documentNumber, issueDate = s.issueDate, expiryDate = s.expiryDate,
                reminderDays = s.reminderDays
            ))
            _addState.update { it.copy(isSaving = false) }
            resetAddState()
            onSuccess()
        }
    }

    fun deleteDocument(id: Long) { viewModelScope.launch { documentRepo.deleteDocument(id) } }
}
