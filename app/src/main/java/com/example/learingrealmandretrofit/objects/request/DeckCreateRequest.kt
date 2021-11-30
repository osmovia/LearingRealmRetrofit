package com.example.learingrealmandretrofit.objects.request

data class DeckCreateRequest(
    val deck: DeckTitleRequest
)

data class DeckTitleRequest(
    val title: String
)
