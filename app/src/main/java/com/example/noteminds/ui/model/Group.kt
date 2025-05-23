package com.example.noteminds.ui.model

// Group.kt
data class Group(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var category: String? = null,
    var createdBy: String? = null,
    var createdAt: Long? = null
)