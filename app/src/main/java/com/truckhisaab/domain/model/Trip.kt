package com.truckhisaab.domain.model

data class Trip(
    val id: Long = 0,
    val fromLocation: String = "",
    val toLocation: String = "",
    val cargoType: String = "",
    val weightTons: Double = 0.0,
    val freightAmount: Double = 0.0,
    val advanceAmount: Double = 0.0,
    val partyName: String = "",
    val driverName: String = "",
    val driverId: Long = 0,
    val truckId: Long = 0,
    val truckNumber: String = "",
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long? = null,
    val status: TripStatus = TripStatus.CREATED,
    val notes: String = "",
    val isSynced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class TripStatus(val label: String, val hindiLabel: String) {
    CREATED("Created", "बनाया"),
    ACTIVE("Active", "चालू"),
    COMPLETED("Completed", "पूरा"),
    CANCELLED("Cancelled", "रद्द")
}

val cargoTypes = listOf(
    "Cement", "Coal", "Grain", "Oil", "Stone", "Sand",
    "Steel", "Timber", "Cotton", "Fertilizer", "Vegetables", "Other"
)

val popularRoutes = listOf(
    "Mumbai" to "Pune", "Delhi" to "Jaipur", "Chennai" to "Bangalore",
    "Ahmedabad" to "Surat", "Kolkata" to "Patna", "Lucknow" to "Kanpur",
    "Hyderabad" to "Vijayawada", "Indore" to "Bhopal"
)
