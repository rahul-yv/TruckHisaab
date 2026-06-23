package com.truckhisaab.data.model

import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    val type: DocumentType = DocumentType.RC,
    val truckId: String? = null,
    val truckNumber: String = "",
    val documentNumber: String = "",
    val issueDate: Long = System.currentTimeMillis(),
    val expiryDate: Long = System.currentTimeMillis(),
    val imageUri: String? = null,
    val reminderDays: List<Int> = listOf(30, 15, 7, 1),
    val renewalHistory: List<RenewalEntry> = emptyList()
)

enum class DocumentType(val label: String, val hindiLabel: String) {
    RC("RC - Registration", "RC"),
    INSURANCE("Insurance", "Insurance"),
    PUC("PUC - Pollution", "PUC"),
    PERMIT("Permit", "Permit"),
    LICENSE("Driver License", "License"),
    FITNESS("Fitness Certificate", "Fitness"),
    OTHER("Other", "Anya")
}

enum class DocumentStatus(val label: String, val hindiLabel: String) {
    VALID("Valid", "Valid"),
    EXPIRING_SOON("Expiring Soon", "Jaldi Expire"),
    EXPIRED("Expired", "Expired")
}

data class RenewalEntry(
    val date: Long = System.currentTimeMillis(),
    val note: String = ""
)

fun getDocumentStatus(expiryDate: Long): DocumentStatus {
    val daysRemaining = (expiryDate - System.currentTimeMillis()) / 86400000L
    return when {
        daysRemaining < 15 -> DocumentStatus.EXPIRED
        daysRemaining < 30 -> DocumentStatus.EXPIRING_SOON
        else -> DocumentStatus.VALID
    }
}
