package com.example.learingrealmandretrofit.deck.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.deck.viewmodel.DeckViewModel

class DeckViewModelFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeckViewModel::class.java)) {
            return DeckViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}