package com.truckhisaab.domain.usecase

import com.truckhisaab.domain.model.User
import com.truckhisaab.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val repo: UserRepository) {
    operator fun invoke(): Flow<User?> = repo.getUser()
    suspend fun sync(): User? = repo.getUserSync()
}

class UpdateProfileUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke(user: User): Long = repo.createOrUpdateUser(user)
}

class LogoutUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke() {
        repo.updateLogin(false)
    }
}

class CompleteOnboardingUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke() = repo.updateOnboarded(true)
}
