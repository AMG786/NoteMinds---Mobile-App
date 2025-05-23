package com.example.noteminds.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteminds.data.room.dao.UserDao
import com.example.noteminds.data.room.entities.Interest
import com.example.noteminds.data.room.entities.User

/**
Created by Abdul Mueez, 04/24/2025
 */
@Database(
    entities = [User::class, Interest::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "learning_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}