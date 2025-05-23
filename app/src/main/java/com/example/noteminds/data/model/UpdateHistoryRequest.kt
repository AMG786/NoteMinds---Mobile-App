package com.example.noteminds.data.model

// Data classes
data class UpdateHistoryRequest(
    val history_id: String,
    val answers: List<String>  // List of user answers (full text of selected options)
)