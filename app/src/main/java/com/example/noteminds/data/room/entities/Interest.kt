package com.example.noteminds.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Created by Abdul Mueez, 04/24/2025
 */
@Entity(tableName = "interests")
data class Interest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val topicName: String
)