package com.example.learingrealmandretrofit.objects.request

import com.google.gson.annotations.SerializedName

data class CardDeckMainRequest(
    @SerializedName("card_deck")
    val cardDeck: CardDeckRequest
)
