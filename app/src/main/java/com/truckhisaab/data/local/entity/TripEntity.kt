package com.truckhisaab.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
    val status: String = "CREATED",
    val notes: String = "",
    val isSynced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
