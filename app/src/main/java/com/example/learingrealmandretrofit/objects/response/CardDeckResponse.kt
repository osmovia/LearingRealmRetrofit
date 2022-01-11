package com.example.learingrealmandretrofit.objects.response

import com.google.gson.annotations.SerializedName

data class CardDeckResponse(
    @SerializedName("card_id")
    val cardId: Int,

    @SerializedName("deck_id")
    val deckId: Int,

    @SerializedName("id")
    val id: Int
)
