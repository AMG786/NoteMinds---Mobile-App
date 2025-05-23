package com.example.noteminds.ui.model
/**
Created by Abdul Mueez, 04/24/2025
 */
data class Task(
    val id: Long =0,
    val title: String,
    val description: String,
    val dueDate: String? = null,
    val status: Status = Status.PENDING,
    val isImportant: Boolean = false,
    var topic: String? = ""
) {
    enum class Status {
        COMPLETED,
        IN_PROGRESS,
        PENDING
    }
}