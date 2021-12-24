package com.example.learingrealmandretrofit.objects.response

import com.google.gson.annotations.SerializedName

data class CardDeckResponse(
    @SerializedName("card_deck")
    val cardDeck: CardDeckIdResponse
)
