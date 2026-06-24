package com.truckhisaab.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fuel_entries")
data class FuelEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
