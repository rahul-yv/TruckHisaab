package com.truckhisaab.data.repository

import com.truckhisaab.data.model.AppNotification
import com.truckhisaab.data.model.NotificationType
import com.truckhisaab.data.model.User
import com.truckhisaab.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepository {
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    val unreadCount: Int get() = _notifications.value.count { !it.isRead }

    init { seedData() }

    fun updateUser(user: User) { _user.value = user }
    fun login(name: String, phone: String, role: UserRole) {
        _user.value = User(name = name, phone = phone, role = role, isOnboarded = true, isLoggedIn = true)
    }
    fun logout() { _user.value = User() }

    fun markNotificationRead(id: String) = _notifications.update { list ->
        list.map { if (it.id == id) it.copy(isRead = true) else it }
    }
    fun markAllRead() = _notifications.update { list -> list.map { it.copy(isRead = true) } }
    fun deleteNotification(id: String) = _notifications.update { it.filter { n -> n.id != id } }

    private fun seedData() {
        _user.value = User(name = "Rajesh Kumar", phone = "+91 98765 43210", role = UserRole.TRUCK_OWNER, isOnboarded = true, isLoggedIn = true, language = "Hinglish")

        val now = System.currentTimeMillis()
        val hour = 3600000L
        _notifications.value = listOf(
            AppNotification(type = NotificationType.TRIP, title = "Trip Complete", message = "Mumbai → Pune trip khatam. Profit: ₹15,700", timestamp = now - 2 * hour, targetRoute = "trip/t1"),
            AppNotification(type = NotificationType.DOCUMENT, title = "PUC Expiring", message = "MH 12 AB 1234 ka PUC 8 din mein expire hoga", timestamp = now - 5 * hour, targetRoute = "documents"),
            AppNotification(type = NotificationType.DOCUMENT, title = "Permit Expired!", message = "HR 26 CD 5678 ka permit expire ho gaya", timestamp = now - 12 * hour, targetRoute = "documents"),
            AppNotification(type = NotificationType.REPORT, title = "Weekly P&L Ready", message = "Is hafte ka hisaab: ₹1,73,000 profit", timestamp = now - 24 * hour, isRead = true, targetRoute = "report"),
            AppNotification(type = NotificationType.SYSTEM, title = "Diesel Rate Update", message = "Diesel ka rate ₹96.50/L ho gaya. Kal se ₹0.30 badha", timestamp = now - 3 * hour),
            AppNotification(type = NotificationType.EXPENSE, title = "Kharcha Reminder", message = "Aaj ka kharcha track karna mat bhulna!", timestamp = now - 8 * hour, isRead = true),
            AppNotification(type = NotificationType.TRIP, title = "Trip Active", message = "Ahmedabad → Surat trip 6 ghante se chalu hai", timestamp = now - 6 * hour, targetRoute = "trip/t3")
        )
    }
}
