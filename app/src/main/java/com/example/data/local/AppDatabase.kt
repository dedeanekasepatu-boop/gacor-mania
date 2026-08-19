package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core.common.Constants
import com.example.data.local.dao.DriverSessionDao
import com.example.data.local.dao.LocationDao
import com.example.data.local.dao.NetworkDao
import com.example.data.local.entity.DriverSessionEntity
import com.example.data.local.entity.LocationSampleEntity
import com.example.data.local.entity.NetworkSampleEntity

@Database(
    entities = [
        LocationSampleEntity::class,
        NetworkSampleEntity::class,
        DriverSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun networkDao(): NetworkDao
    abstract fun driverSessionDao(): DriverSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
