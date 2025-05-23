package com.example.noteminds.data.model

import com.google.gson.annotations.SerializedName

// RegisterResponse.kt
data class RegisterResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user_id") val userId: String
)