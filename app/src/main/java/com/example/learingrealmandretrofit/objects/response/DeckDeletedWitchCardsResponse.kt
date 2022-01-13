package com.example.learingrealmandretrofit.objects.response

import com.example.learingrealmandretrofit.objects.CardParameters
import com.google.gson.annotations.SerializedName

data class DeckDeletedWitchCardsResponse(
    @SerializedName("deleted_cards")
    val deletedCards: List<CardParameters>
)
