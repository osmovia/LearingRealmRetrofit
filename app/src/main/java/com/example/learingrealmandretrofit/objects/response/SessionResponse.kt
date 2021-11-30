package com.example.learingrealmandretrofit.objects.response

import com.google.gson.annotations.SerializedName

data class SessionResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("token")
    val token: String,

    @SerializedName("user_id")
    val userId: Int
)
