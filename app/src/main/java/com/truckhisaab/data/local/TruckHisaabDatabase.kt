package com.truckhisaab.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.truckhisaab.data.local.dao.*
import com.truckhisaab.data.local.entity.*

@Database(
    entities = [
        TripEntity::class,
        ExpenseEntity::class,
        FuelEntryEntity::class,
        TruckEntity::class,
        DriverEntity::class,
        DocumentEntity::class,
        UserEntity::class,
        NotificationEntity::class,
        SyncQueueEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class TruckHisaabDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun fuelEntryDao(): FuelEntryDao
    abstract fun truckDao(): TruckDao
    abstract fun driverDao(): DriverDao
    abstract fun documentDao(): DocumentDao
    abstract fun userDao(): UserDao
    abstract fun notificationDao(): NotificationDao
    abstract fun syncQueueDao(): SyncQueueDao
}
