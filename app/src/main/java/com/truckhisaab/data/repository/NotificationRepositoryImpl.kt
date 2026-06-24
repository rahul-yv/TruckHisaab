package com.truckhisaab.data.repository

import com.truckhisaab.data.local.dao.NotificationDao
import com.truckhisaab.data.mapper.toDomain
import com.truckhisaab.data.mapper.toEntity
import com.truckhisaab.domain.model.AppNotification
import com.truckhisaab.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun getAllNotifications(): Flow<List<AppNotification>> =
        notificationDao.getAllNotifications().map { list -> list.map { it.toDomain() } }

    override fun getUnreadCount(): Flow<Int> =
        notificationDao.getUnreadCount()

    override suspend fun addNotification(notification: AppNotification): Long =
        notificationDao.insert(notification.toEntity())

    override suspend fun markAsRead(id: Long) =
        notificationDao.markAsRead(id)

    override suspend fun markAllAsRead() =
        notificationDao.markAllAsRead()

    override suspend fun deleteAll() =
        notificationDao.deleteAll()
}
