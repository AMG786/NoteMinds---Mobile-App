package com.example.noteminds.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username_or_email") val usernameOrEmail: String,
    @SerializedName("password") val password: String
)