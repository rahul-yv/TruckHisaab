package com.truckhisaab.domain.model

data class Driver(
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val licenseNumber: String = "",
    val licenseExpiry: Long = 0,
    val monthlySalary: Double = 0.0,
    val advanceTaken: Double = 0.0,
    val photoUri: String? = null,
    val isActive: Boolean = true,
    val totalTrips: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
