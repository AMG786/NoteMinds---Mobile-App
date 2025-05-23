package com.example.noteminds.data.room.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
Created by Abdul Mueez, 04/24/2025
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val phone: String?
)