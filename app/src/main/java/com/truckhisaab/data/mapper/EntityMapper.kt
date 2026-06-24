package com.truckhisaab.data.mapper

import com.truckhisaab.data.local.entity.*
import com.truckhisaab.domain.model.*

fun TripEntity.toDomain() = Trip(
    id = id, fromLocation = fromLocation, toLocation = toLocation,
    cargoType = cargoType, weightTons = weightTons, freightAmount = freightAmount,
    advanceAmount = advanceAmount, partyName = partyName, driverName = driverName,
    driverId = driverId, truckId = truckId, truckNumber = truckNumber,
    startDate = startDate, endDate = endDate,
    status = TripStatus.entries.find { it.name == status } ?: TripStatus.CREATED,
    notes = notes, isSynced = isSynced, createdAt = createdAt
)

fun Trip.toEntity() = TripEntity(
    id = id, fromLocation = fromLocation, toLocation = toLocation,
    cargoType = cargoType, weightTons = weightTons, freightAmount = freightAmount,
    advanceAmount = advanceAmount, partyName = partyName, driverName = driverName,
    driverId = driverId, truckId = truckId, truckNumber = truckNumber,
    startDate = startDate, endDate = endDate, status = status.name,
    notes = notes, isSynced = isSynced, createdAt = createdAt
)

fun ExpenseEntity.toDomain() = Expense(
    id = id, tripId = tripId, truckId = truckId, truckNumber = truckNumber,
    category = ExpenseCategory.entries.find { it.name == category } ?: ExpenseCategory.OTHER,
    amount = amount, date = date, location = location, note = note,
    receiptUri = receiptUri, isSynced = isSynced, createdAt = createdAt
)

fun Expense.toEntity() = ExpenseEntity(
    id = id, tripId = tripId, truckId = truckId, truckNumber = truckNumber,
    category = category.name, amount = amount, date = date, location = location,
    note = note, receiptUri = receiptUri, isSynced = isSynced, createdAt = createdAt
)

fun FuelEntryEntity.toDomain() = FuelEntry(
    id = id, truckId = truckId, truckNumber = truckNumber, date = date,
    odometer = odometer, liters = liters, pricePerLiter = pricePerLiter,
    totalAmount = totalAmount, location = location, isSynced = isSynced
)

fun FuelEntry.toEntity() = FuelEntryEntity(
    id = id, truckId = truckId, truckNumber = truckNumber, date = date,
    odometer = odometer, liters = liters, pricePerLiter = pricePerLiter,
    totalAmount = totalAmount, location = location, isSynced = isSynced
)

fun TruckEntity.toDomain() = Truck(
    id = id, number = number,
    type = TruckType.entries.find { it.name == type } ?: TruckType.OPEN_BODY,
    manufacturer = manufacturer, model = model, yearOfPurchase = yearOfPurchase,
    currentOdometer = currentOdometer, isActive = isActive, photoUri = photoUri,
    createdAt = createdAt
)

fun Truck.toEntity() = TruckEntity(
    id = id, number = number, type = type.name, manufacturer = manufacturer,
    model = model, yearOfPurchase = yearOfPurchase, currentOdometer = currentOdometer,
    isActive = isActive, photoUri = photoUri, createdAt = createdAt
)

fun DriverEntity.toDomain() = Driver(
    id = id, name = name, phone = phone, licenseNumber = licenseNumber,
    licenseExpiry = licenseExpiry, monthlySalary = monthlySalary,
    advanceTaken = advanceTaken, photoUri = photoUri, isActive = isActive,
    totalTrips = totalTrips, createdAt = createdAt
)

fun Driver.toEntity() = DriverEntity(
    id = id, name = name, phone = phone, licenseNumber = licenseNumber,
    licenseExpiry = licenseExpiry, monthlySalary = monthlySalary,
    advanceTaken = advanceTaken, photoUri = photoUri, isActive = isActive,
    totalTrips = totalTrips, createdAt = createdAt
)

fun DocumentEntity.toDomain() = Document(
    id = id,
    type = DocumentType.entries.find { it.name == type } ?: DocumentType.OTHER,
    truckId = truckId, truckNumber = truckNumber, documentNumber = documentNumber,
    issueDate = issueDate, expiryDate = expiryDate, imageUri = imageUri,
    reminderDays = reminderDays.split(",").mapNotNull { it.trim().toIntOrNull() },
    isSynced = isSynced, createdAt = createdAt
)

fun Document.toEntity() = DocumentEntity(
    id = id, type = type.name, truckId = truckId, truckNumber = truckNumber,
    documentNumber = documentNumber, issueDate = issueDate, expiryDate = expiryDate,
    imageUri = imageUri, reminderDays = reminderDays.joinToString(","),
    isSynced = isSynced, createdAt = createdAt
)

fun UserEntity.toDomain() = User(
    id = id, name = name, phone = phone, language = language,
    isOnboarded = isOnboarded, isLoggedIn = isLoggedIn,
    biometricEnabled = biometricEnabled, profilePhotoUri = profilePhotoUri,
    createdAt = createdAt, lastLogin = lastLogin
)

fun User.toEntity() = UserEntity(
    id = id, name = name, phone = phone, language = language,
    isOnboarded = isOnboarded, isLoggedIn = isLoggedIn,
    biometricEnabled = biometricEnabled, profilePhotoUri = profilePhotoUri,
    createdAt = createdAt, lastLogin = lastLogin
)

fun NotificationEntity.toDomain() = AppNotification(
    id = id,
    type = NotificationType.entries.find { it.name == type } ?: NotificationType.SYSTEM,
    title = title, message = message, timestamp = timestamp,
    isRead = isRead, targetRoute = targetRoute
)

fun AppNotification.toEntity() = NotificationEntity(
    id = id, type = type.name, title = title, message = message,
    timestamp = timestamp, isRead = isRead, targetRoute = targetRoute
)
