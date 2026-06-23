package com.truckhisaab.data.model

import java.util.UUID

data class Driver(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val phone: String = "",
    val licenseNumber: String = "",
    val licenseExpiry: Long = System.currentTimeMillis(),
    val monthlySalary: Double = 0.0,
    val advanceTaken: Double = 0.0,
    val photoUri: String? = null,
    val isActive: Boolean = true,
    val totalTrips: Int = 0
)
