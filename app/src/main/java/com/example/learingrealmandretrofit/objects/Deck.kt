package com.example.learingrealmandretrofit.objects

data class Deck(
    val id: Int = 0,
    val title: String,
    val cards: List<Card>
)