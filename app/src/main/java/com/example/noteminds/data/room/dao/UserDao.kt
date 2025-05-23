package com.example.noteminds.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.noteminds.data.room.entities.Interest
import com.example.noteminds.data.room.entities.User

/**
Created by Abdul Mueez, 04/24/2025
 */
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Insert
    suspend fun insertInterest(interest: Interest)

    // Add this new method
    @Insert
    suspend fun insertInterests(interests: List<Interest>)

    // Keep all existing methods
    @Query("DELETE FROM interests WHERE userId = :userId")
    suspend fun clearUserInterests(userId: Int)

    @Query("SELECT topicName FROM interests WHERE userId = :userId")
    suspend fun getUserInterests(userId: Int): List<String>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun checkUsernameExists(username: String): Boolean
}