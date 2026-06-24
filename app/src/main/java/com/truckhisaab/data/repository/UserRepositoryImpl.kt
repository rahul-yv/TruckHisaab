package com.truckhisaab.data.repository

import com.truckhisaab.data.local.dao.UserDao
import com.truckhisaab.data.mapper.toDomain
import com.truckhisaab.data.mapper.toEntity
import com.truckhisaab.domain.model.User
import com.truckhisaab.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getUser(): Flow<User?> =
        userDao.getUser().map { it?.toDomain() }

    override suspend fun getUserSync(): User? =
        userDao.getUserSync()?.toDomain()

    override suspend fun createOrUpdateUser(user: User): Long =
        userDao.insert(user.toEntity())

    override suspend fun updateOnboarded(onboarded: Boolean) {
        val existing = userDao.getUserSync() ?: return
        userDao.update(existing.copy(isOnboarded = onboarded))
    }

    override suspend fun updateLogin(loggedIn: Boolean) {
        val existing = userDao.getUserSync() ?: return
        userDao.update(existing.copy(isLoggedIn = loggedIn, lastLogin = System.currentTimeMillis()))
    }

    override suspend fun deleteUser() = userDao.deleteAll()
}
