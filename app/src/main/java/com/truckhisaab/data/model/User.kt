package com.truckhisaab.data.model

data class User(
    val name: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.TRUCK_OWNER,
    val isOnboarded: Boolean = false,
    val isLoggedIn: Boolean = false,
    val language: String = "Hinglish"
)

enum class UserRole(val label: String, val hindiLabel: String) {
    TRUCK_OWNER("Truck Owner", "Truck Maalik"),
    DRIVER("Driver", "Driver"),
    FLEET_OWNER("Fleet Owner", "Fleet Maalik"),
    BROKER("Broker", "Broker")
}
