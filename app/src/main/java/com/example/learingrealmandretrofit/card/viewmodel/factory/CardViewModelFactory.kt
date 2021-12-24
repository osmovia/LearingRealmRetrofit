package com.example.learingrealmandretrofit.card.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.CardViewModel

class CardViewModelFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            return CardViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}