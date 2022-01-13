package com.example.learingrealmandretrofit.deck.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.deck.viewmodel.DeleteDeckViewModel

class DeleteDeckViewModelFactory(private val application: Application, private val deckId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteDeckViewModel::class.java)) {
            return DeleteDeckViewModel(application, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}