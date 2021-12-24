package com.example.learingrealmandretrofit.deck.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.deck.viewmodel.DeleteDeckViewModel

class DeleteDeckViewModelFactory(private val token: String, private val deckId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteDeckViewModel::class.java)) {
            return DeleteDeckViewModel(token, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}