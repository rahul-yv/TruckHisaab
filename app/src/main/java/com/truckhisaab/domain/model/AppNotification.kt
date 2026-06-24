package com.truckhisaab.domain.model

data class AppNotification(
    val id: Long = 0,
    val type: NotificationType = NotificationType.SYSTEM,
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val targetRoute: String? = null
)

enum class NotificationType {
    TRIP, EXPENSE, DOCUMENT, REPORT, SYSTEM
}
