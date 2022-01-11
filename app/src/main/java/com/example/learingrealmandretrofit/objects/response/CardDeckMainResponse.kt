package com.example.learingrealmandretrofit.objects.response

import com.google.gson.annotations.SerializedName

data class CardDeckMainResponse(
    @SerializedName("card_deck")
    val cardDeck: CardDeckResponse
)
