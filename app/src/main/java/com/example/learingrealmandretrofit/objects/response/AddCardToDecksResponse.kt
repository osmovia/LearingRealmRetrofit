package com.example.learingrealmandretrofit.objects.response

import com.google.gson.annotations.SerializedName

data class AddCardToDecksResponse(

    @SerializedName("created_card_decks")
    val createdCardDecks: List<CardDeckIdResponse>,

    @SerializedName("deleted_card_decks")
    val deletedCardDecks: List<CardDeckIdResponse>
)
