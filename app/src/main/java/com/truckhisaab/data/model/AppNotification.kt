package com.truckhisaab.data.model

import java.util.UUID

data class AppNotification(
    val id: String = UUID.randomUUID().toString(),
    val type: NotificationType = NotificationType.SYSTEM,
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val targetRoute: String? = null
)

enum class NotificationType { TRIP, EXPENSE, DOCUMENT, REPORT, SYSTEM }
