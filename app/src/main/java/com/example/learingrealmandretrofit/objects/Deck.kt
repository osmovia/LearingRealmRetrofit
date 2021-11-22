package com.example.learingrealmandretrofit.objects

import java.io.Serializable

data class Deck(
    val id: Int = 0,
    val title: String,
    val cards: List<Card>
) : Serializable
