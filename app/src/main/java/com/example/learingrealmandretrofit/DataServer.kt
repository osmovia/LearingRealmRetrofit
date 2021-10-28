package com.example.learingrealmandretrofit

data class DataServer(val cards: List<RealmCard>)

data class CardX(
    val example: String,
    val id: Int,
    val translation: String,
    val word: String
)