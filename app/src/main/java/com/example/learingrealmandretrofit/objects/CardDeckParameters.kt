package com.example.learingrealmandretrofit.objects

import com.google.gson.annotations.SerializedName

data class CardDeckParameters(

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("card_id")
    val cardId: Int,

    @SerializedName("deck_id")
    val deckId: Int
)
