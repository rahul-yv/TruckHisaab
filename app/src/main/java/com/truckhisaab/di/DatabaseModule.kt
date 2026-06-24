package com.truckhisaab.di

import android.content.Context
import androidx.room.Room
import com.truckhisaab.data.local.TruckHisaabDatabase
import com.truckhisaab.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TruckHisaabDatabase =
        Room.databaseBuilder(context, TruckHisaabDatabase::class.java, "truckhisaab.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideTripDao(db: TruckHisaabDatabase): TripDao = db.tripDao()
    @Provides fun provideExpenseDao(db: TruckHisaabDatabase): ExpenseDao = db.expenseDao()
    @Provides fun provideFuelEntryDao(db: TruckHisaabDatabase): FuelEntryDao = db.fuelEntryDao()
    @Provides fun provideTruckDao(db: TruckHisaabDatabase): TruckDao = db.truckDao()
    @Provides fun provideDriverDao(db: TruckHisaabDatabase): DriverDao = db.driverDao()
    @Provides fun provideDocumentDao(db: TruckHisaabDatabase): DocumentDao = db.documentDao()
    @Provides fun provideUserDao(db: TruckHisaabDatabase): UserDao = db.userDao()
    @Provides fun provideNotificationDao(db: TruckHisaabDatabase): NotificationDao = db.notificationDao()
    @Provides fun provideSyncQueueDao(db: TruckHisaabDatabase): SyncQueueDao = db.syncQueueDao()
}
