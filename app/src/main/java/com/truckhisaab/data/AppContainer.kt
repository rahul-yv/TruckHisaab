package com.truckhisaab.data

import com.truckhisaab.data.repository.DocumentRepository
import com.truckhisaab.data.repository.DriverRepository
import com.truckhisaab.data.repository.ExpenseRepository
import com.truckhisaab.data.repository.TripRepository
import com.truckhisaab.data.repository.TruckRepository
import com.truckhisaab.data.repository.UserRepository

object AppContainer {
    val tripRepository = TripRepository()
    val expenseRepository = ExpenseRepository()
    val documentRepository = DocumentRepository()
    val truckRepository = TruckRepository()
    val driverRepository = DriverRepository()
    val userRepository = UserRepository()
}
