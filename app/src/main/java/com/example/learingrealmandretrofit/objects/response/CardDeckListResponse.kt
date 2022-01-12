package com.example.learingrealmandretrofit.objects.response

import com.example.learingrealmandretrofit.objects.CardDeckParameters
import com.google.gson.annotations.SerializedName

data class CardDeckListResponse(
    @SerializedName("card_decks")
    val cardDecks: List<CardDeckParameters>)

