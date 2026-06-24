package com.truckhisaab.domain.model

data class User(
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val language: String = "hi",
    val isOnboarded: Boolean = false,
    val isLoggedIn: Boolean = false,
    val biometricEnabled: Boolean = false,
    val profilePhotoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLogin: Long = System.currentTimeMillis()
)

enum class UserRole(val hindiLabel: String) {
    OWNER("मालिक"),
    DRIVER("ड्राइवर"),
    MANAGER("मैनेजर")
}
