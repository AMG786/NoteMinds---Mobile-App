package com.example.noteminds

import android.app.Application
import com.example.noteminds.data.repository.UserRepository
import com.example.noteminds.data.room.AppDatabase
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getDatabase(this)
        FirebaseApp.initializeApp(this)
        userRepository = UserRepository(database.userDao())
    }
}