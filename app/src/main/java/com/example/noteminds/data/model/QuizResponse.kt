package com.example.noteminds.data.model

import com.google.gson.annotations.SerializedName

data class QuizResponse(
    @SerializedName("quiz")
    val quiz: List<QuizQuestion>,
    @SerializedName("history_id")
    val history_id: String
)