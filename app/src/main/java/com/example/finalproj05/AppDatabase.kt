package com.example.finalproj05


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Defines the db and tells Room which entry's the db will hold
@Database(entities = [FoodEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    // Allows access to the db so the rest of the app can query it
    abstract fun foodDao(): FoodDao

    // holds the singleton pattern of the db
    companion object {

        @Volatile
        //Makes it immediately visible to other threads
        private var db: AppDatabase? = null

        // returns database if it exists or creates it if it doesn't
        fun getData(context: Context): AppDatabase =
            db ?: synchronized(this) {
                db ?: foodDatabase(context).also { db = it }
            }

        // builds the db
        private fun foodDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Foods-db2"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}