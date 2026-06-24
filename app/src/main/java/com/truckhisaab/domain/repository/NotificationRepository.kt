package com.truckhisaab.domain.repository

import com.truckhisaab.domain.model.AppNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(): Flow<List<AppNotification>>
    fun getUnreadCount(): Flow<Int>
    suspend fun addNotification(notification: AppNotification): Long
    suspend fun markAsRead(id: Long)
    suspend fun markAllAsRead()
    suspend fun deleteAll()
}
