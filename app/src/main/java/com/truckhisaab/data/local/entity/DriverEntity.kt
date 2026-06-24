package com.truckhisaab.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
