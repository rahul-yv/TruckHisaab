package com.truckhisaab.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: Long? = null,
    val truckId: Long = 0,
    val truckNumber: String = "",
    val category: String = "OTHER",
    val amount: Double = 0.0,
    val date: Long = System.currentTimeMillis(),
    val location: String = "",
    val note: String = "",
    val receiptUri: String? = null,
    val isSynced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
