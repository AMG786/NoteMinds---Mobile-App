package com.example.noteminds.data.model

import com.google.gson.annotations.SerializedName

data class InterestRequest(
    @SerializedName("interests") val interests: List<String>
)
