package com.example.learingrealmandretrofit.objects

data class SessionSharedPreferencesWrite(
    val email: String,
    val sessionToken: String,
    val userId: Int
)
