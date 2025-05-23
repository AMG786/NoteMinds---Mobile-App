package com.example.noteminds.ui.model

import java.io.Serializable
/**
Created by Abdul Mueez, 04/24/2025
 */

data class Question(
    val id: Long=0,
    val number: Int=0,
    val title: String="",
    val questionText: String="",
    val questionType: QuestionType?,
    val options: List<String>,
    var correctAnswer: String="",
    var selectedOption: Int = -1,
    var userAnswer: String = "",
    var isAnswered: Boolean = false,
    var status:Boolean = false
): Serializable {
    fun toQuestion(): Question {
        return Question(
            id = 0, // Will be set properly when mapping with index
            number = 0, // Will be set properly when mapping with index
            title = "", // Will be set properly when mapping with index
            questionText = questionText,
            questionType = null, // Assuming all are multiple choice
            options = this.options,
            correctAnswer = correctAnswer,
            selectedOption = -1, // Initialize as unanswered
            isAnswered = false,
            status = false
        )
    }
    enum class QuestionType {
        MULTIPLE_CHOICE,
        TOGGLE
    }
}