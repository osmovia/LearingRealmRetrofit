package com.example.learingrealmandretrofit.card.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.ShowDetailsCardViewModel

class ShowDetailsCardViewModelFactory(private val token: String, private val cardId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowDetailsCardViewModel::class.java)) {
            return ShowDetailsCardViewModel(token, cardId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
