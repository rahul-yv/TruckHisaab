package com.truckhisaab.data.model

import java.util.UUID

data class Trip(
    val id: String = UUID.randomUUID().toString(),
    val fromLocation: String = "",
    val toLocation: String = "",
    val cargoType: String = "",
    val weightTons: Double = 0.0,
    val freightAmount: Double = 0.0,
    val advanceAmount: Double = 0.0,
    val partyName: String = "",
    val driverName: String = "",
    val driverId: String? = null,
    val truckId: String? = null,
    val truckNumber: String = "",
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long? = null,
    val status: TripStatus = TripStatus.ACTIVE
)

enum class TripStatus(val label: String, val hindiLabel: String) {
    ACTIVE("Active", "Chalu"),
    COMPLETED("Completed", "Khatam"),
    CANCELLED("Cancelled", "Radd")
}

val cargoTypes = listOf(
    "Cement", "Coal", "Steel", "Sand", "Stone",
    "Oil", "Grain", "Fertilizer", "Cotton", "Timber",
    "Petroleum", "Chemicals", "Electronics", "Furniture", "Other"
)

val popularRoutes = listOf(
    "Delhi" to "Jaipur",
    "Mumbai" to "Pune",
    "Chennai" to "Bangalore",
    "Ahmedabad" to "Surat",
    "Lucknow" to "Kanpur",
    "Kolkata" to "Bhubaneswar",
    "Hyderabad" to "Vijayawada",
    "Indore" to "Bhopal"
)
