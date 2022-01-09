package com.example.learingrealmandretrofit.objects.request

import com.google.gson.annotations.SerializedName

data class AddCardToDecksRequest(
    @SerializedName("deck_ids")
    val deckIds: List<Int>
)
