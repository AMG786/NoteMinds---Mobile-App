package com.example.noteminds.data.model

data class PdfUploadResponse(
    val summary: String,
    val quiz: List<QuizQuestion>
)