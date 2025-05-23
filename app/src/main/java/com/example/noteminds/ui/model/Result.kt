package com.example.noteminds.ui.model
/**
Created by Abdul Mueez, 04/24/2025
 */
data class Result(
    val id: Long,
    val number: Int,
    val questionTitle: String,
    val questionText: String="",
    val correctAnswer: String="",
    val userAnswer: String="",
    val isCorrect: Boolean=false,
    val explanation: String? = null
)