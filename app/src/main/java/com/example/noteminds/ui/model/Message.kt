package com.example.noteminds.ui.model

data class Message(
    val text: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val timestamp: Long = 0
)