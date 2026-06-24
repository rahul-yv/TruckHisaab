package com.truckhisaab.domain.repository

import com.truckhisaab.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User?>
    suspend fun getUserSync(): User?
    suspend fun createOrUpdateUser(user: User): Long
    suspend fun updateOnboarded(onboarded: Boolean)
    suspend fun updateLogin(loggedIn: Boolean)
    suspend fun deleteUser()
}
