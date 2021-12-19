package com.example.learingrealmandretrofit.card.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.DeleteCardViewModel

class DeleteCardViewModelFactory(private val token: String, private val cardId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteCardViewModel::class.java)) {
            return DeleteCardViewModel(token, cardId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}