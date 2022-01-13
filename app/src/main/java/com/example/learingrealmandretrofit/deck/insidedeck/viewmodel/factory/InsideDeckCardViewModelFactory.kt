package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckCardViewModel

class InsideDeckCardViewModelFactory(private val application: Application, private val deckId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsideDeckCardViewModel::class.java)) {
            return InsideDeckCardViewModel(application, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
