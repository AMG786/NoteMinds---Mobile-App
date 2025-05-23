package com.example.noteminds.data.model

import com.google.gson.annotations.SerializedName

// ApiQuestion.kt (temporary class for API response)
data class ApiQuestion(
    val question: String,
    val options: List<String>,
    @SerializedName("correct_answer") val correctAnswer: String?,
    @SerializedName("user_answer") val userAnswer: String?,
    @SerializedName("is_correct") val isCorrect: Boolean?
)