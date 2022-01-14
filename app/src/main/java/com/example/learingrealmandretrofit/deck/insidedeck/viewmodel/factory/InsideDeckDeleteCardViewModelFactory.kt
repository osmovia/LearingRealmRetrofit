package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.deck.insidedeck.viewmodel.InsideDeckDeleteCardViewModel

class InsideDeckDeleteCardViewModelFactory(
    private val application: Application,
    private val cardDeckId: Int
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsideDeckDeleteCardViewModel::class.java)) {
            return InsideDeckDeleteCardViewModel(application, cardDeckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
