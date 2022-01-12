package com.example.learingrealmandretrofit.objects.request

import com.google.gson.annotations.SerializedName

data class CardDeckRequest(
    @SerializedName("card_id")
    val cardId: Int,

    @SerializedName("deck_id")
    val deckId: Int
)
