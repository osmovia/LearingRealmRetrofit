package com.example.learingrealmandretrofit.model

data class Post(val cards: List<QuestListItem>)

data class QuestListItem(
    val id: Int,
    val word: String,
    val translation: String,
    val example: String
)