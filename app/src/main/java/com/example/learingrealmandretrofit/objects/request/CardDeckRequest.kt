package com.example.learingrealmandretrofit.objects.request

import com.google.gson.annotations.SerializedName

data class CardDeckRequest(
    @SerializedName("card_deck")
    val cardDeck: CardDeckIdRequest
)
