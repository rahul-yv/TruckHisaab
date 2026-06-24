package com.truckhisaab.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String = "RC",
    val truckId: Long = 0,
    val truckNumber: String = "",
    val documentNumber: String = "",
    val issueDate: Long = 0,
    val expiryDate: Long = 0,
    val imageUri: String? = null,
    val reminderDays: String = "30,15,7",
    val isSynced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
