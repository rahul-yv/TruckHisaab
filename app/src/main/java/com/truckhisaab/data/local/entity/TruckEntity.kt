package com.truckhisaab.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucks")
data class TruckEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val number: String = "",
    val type: String = "OPEN_BODY",
    val manufacturer: String = "",
    val model: String = "",
    val yearOfPurchase: Int = 2020,
    val currentOdometer: Int = 0,
    val isActive: Boolean = true,
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
