package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckDeleteCardViewModel

class InsideDeckDeleteCardViewModelFactory(private val token: String, private val deckId: Int, private val cardId: Int, private val cardDeckId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsideDeckDeleteCardViewModel::class.java)) {
            return InsideDeckDeleteCardViewModel(token, deckId, cardId, cardDeckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
