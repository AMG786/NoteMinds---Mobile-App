package com.example.noteminds.data.repository

import com.example.noteminds.data.room.dao.UserDao
import com.example.noteminds.data.room.entities.User
import com.example.noteminds.data.room.entities.Interest

/**
Created by Abdul Mueez, 04/24/2025
 */
class UserRepository(private val userDao: UserDao) {

    suspend fun getUser(username: String, password: String): User? {
    return userDao.getUser(username, password)
}


    suspend fun login(username: String, password: String): User? =
        userDao.getUser(username, password)

    suspend fun addUserInterest(userId: Int, topics: List<String>) {
        topics.forEach { topic ->
            userDao.insertInterest(Interest(userId = userId, topicName = topic))
        }
    }

    suspend fun getUserInterests(userId: Int): List<String> =
        userDao.getUserInterests(userId)
    suspend fun registerUser(user: User): Long = userDao.insertUser(user)

    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }
}