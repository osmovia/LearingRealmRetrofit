package com.example.learingrealmandretrofit.card.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.AddCardToDeckViewModel

class AddCardToDeckViewModelFactory(private val token: String, private val cardId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCardToDeckViewModel::class.java)) {
            return AddCardToDeckViewModel(token, cardId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}