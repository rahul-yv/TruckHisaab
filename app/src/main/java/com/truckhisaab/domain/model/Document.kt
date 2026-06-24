package com.truckhisaab.domain.model

data class Document(
    val id: Long = 0,
    val type: DocumentType = DocumentType.RC,
    val truckId: Long = 0,
    val truckNumber: String = "",
    val documentNumber: String = "",
    val issueDate: Long = 0,
    val expiryDate: Long = 0,
    val imageUri: String? = null,
    val reminderDays: List<Int> = listOf(30, 15, 7),
    val isSynced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class DocumentType(val label: String, val hindiLabel: String) {
    RC("RC", "आरसी"),
    INSURANCE("Insurance", "बीमा"),
    PUC("PUC", "पीयूसी"),
    FITNESS("Fitness", "फिटनेस"),
    PERMIT("Permit", "परमिट"),
    LICENSE("License", "लाइसेंस"),
    ROAD_TAX("Road Tax", "रोड टैक्स"),
    OTHER("Other", "अन्य")
}

enum class DocumentStatus(val label: String, val hindiLabel: String) {
    VALID("Valid", "वैध"),
    EXPIRING_SOON("Expiring Soon", "जल्दी खत्म"),
    EXPIRED("Expired", "खत्म")
}

fun getDocumentStatus(expiryDate: Long): DocumentStatus {
    val daysLeft = ((expiryDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
    return when {
        daysLeft < 0 -> DocumentStatus.EXPIRED
        daysLeft <= 30 -> DocumentStatus.EXPIRING_SOON
        else -> DocumentStatus.VALID
    }
}
