package com.example.learingrealmandretrofit

import java.io.Serializable
import java.util.*

data class Card(
    var word: String,
    var translate: String,
    val id: String = UUID.randomUUID().toString()
) : Serializable