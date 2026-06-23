package com.truckhisaab.data.model

import java.util.UUID

data class Truck(
    val id: String = UUID.randomUUID().toString(),
    val number: String = "",
    val type: TruckType = TruckType.HCV,
    val manufacturer: String = "",
    val model: String = "",
    val yearOfPurchase: Int = 2020,
    val currentOdometer: Long = 0,
    val isActive: Boolean = true,
    val photoUri: String? = null
)

enum class TruckType(val label: String) {
    MINI("Mini"),
    LCV("LCV"),
    HCV("HCV"),
    TANKER("Tanker"),
    CONTAINER("Container"),
    TRAILER("Trailer"),
    OTHER("Other")
}

val truckManufacturers = listOf(
    "Tata", "Ashok Leyland", "BharatBenz", "Mahindra",
    "Eicher", "Volvo", "Scania", "MAN", "Other"
)
