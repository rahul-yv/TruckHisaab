package com.truckhisaab.domain.model

data class Truck(
    val id: Long = 0,
    val number: String = "",
    val type: TruckType = TruckType.OPEN_BODY,
    val manufacturer: String = "",
    val model: String = "",
    val yearOfPurchase: Int = 2020,
    val currentOdometer: Int = 0,
    val isActive: Boolean = true,
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class TruckType(val label: String, val hindiLabel: String) {
    OPEN_BODY("Open Body", "खुला बॉडी"),
    CONTAINER("Container", "कंटेनर"),
    TANKER("Tanker", "टैंकर"),
    TRAILER("Trailer", "ट्रेलर"),
    TIPPER("Tipper", "टिपर"),
    FLATBED("Flatbed", "फ्लैटबेड")
}

val truckManufacturers = listOf(
    "Tata", "Ashok Leyland", "BharatBenz", "Mahindra",
    "Eicher", "Volvo", "Scania", "MAN", "AMW"
)
