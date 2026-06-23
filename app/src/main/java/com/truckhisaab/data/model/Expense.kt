package com.truckhisaab.data.model

import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val tripId: String? = null,
    val truckId: String? = null,
    val category: ExpenseCategory = ExpenseCategory.OTHER,
    val amount: Double = 0.0,
    val date: Long = System.currentTimeMillis(),
    val location: String = "",
    val note: String = "",
    val receiptUri: String? = null
)

enum class ExpenseCategory(val label: String, val hindiLabel: String, val colorHex: Long) {
    DIESEL("Diesel", "Diesel", 0xFFCD2D28),
    TOLL("Toll", "Toll", 0xFFFF9800),
    TYRE("Tyre", "Tyre", 0xFF2196F3),
    REPAIR("Repair", "Repair", 0xFF9C27B0),
    FOOD("Food", "Khaana", 0xFF4CAF50),
    PARKING("Parking", "Parking", 0xFF607D8B),
    OTHER("Other", "Anya", 0xFF9E9E9E)
}

data class FuelEntry(
    val id: String = UUID.randomUUID().toString(),
    val truckId: String = "",
    val date: Long = System.currentTimeMillis(),
    val odometer: Long = 0,
    val liters: Double = 0.0,
    val pricePerLiter: Double = 0.0,
    val totalAmount: Double = 0.0,
    val location: String = "",
    val kmPerLiter: Double = 0.0
)
