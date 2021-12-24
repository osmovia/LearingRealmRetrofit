package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckUpdateCardViewModel

class InsideDeckUpdateCardViewModelFactory(private val token: String, private val deckId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsideDeckUpdateCardViewModel::class.java)) {
            return InsideDeckUpdateCardViewModel(token, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
