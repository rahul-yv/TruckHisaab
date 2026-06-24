package com.truckhisaab.domain.model

data class Expense(
    val id: Long = 0,
    val tripId: Long? = null,
    val truckId: Long = 0,
    val truckNumber: String = "",
    val category: ExpenseCategory = ExpenseCategory.OTHER,
    val amount: Double = 0.0,
    val date: Long = System.currentTimeMillis(),
    val location: String = "",
    val note: String = "",
    val receiptUri: String? = null,
    val isSynced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ExpenseCategory(
    val label: String,
    val hindiLabel: String,
    val colorHex: Long
) {
    DIESEL("Diesel", "डीज़ल", 0xFFE53935),
    TOLL("Toll", "टोल", 0xFF1E88E5),
    TYRE("Tyre", "टायर", 0xFF43A047),
    REPAIR("Repair", "मरम्मत", 0xFFFF9800),
    FOOD("Food", "खाना", 0xFF8E24AA),
    PARKING("Parking", "पार्किंग", 0xFF00ACC1),
    OTHER("Other", "अन्य", 0xFF757575)
}

data class FuelEntry(
    val id: Long = 0,
    val truckId: Long = 0,
    val truckNumber: String = "",
    val date: Long = System.currentTimeMillis(),
    val odometer: Int = 0,
    val liters: Double = 0.0,
    val pricePerLiter: Double = 0.0,
    val totalAmount: Double = 0.0,
    val location: String = "",
    val isSynced: Boolean = false
)

data class CategoryBreakdown(
    val category: ExpenseCategory,
    val total: Double,
    val count: Int,
    val percentage: Double
)
