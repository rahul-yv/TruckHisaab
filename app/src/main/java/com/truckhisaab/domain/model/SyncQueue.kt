package com.truckhisaab.domain.model

data class SyncQueue(
    val id: Long = 0,
    val action: SyncAction = SyncAction.CREATE,
    val entityType: String = "",
    val entityId: Long = 0,
    val payload: String = "",
    val retryCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class SyncAction { CREATE, UPDATE, DELETE }
