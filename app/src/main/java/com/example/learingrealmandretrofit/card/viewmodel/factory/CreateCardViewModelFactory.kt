package com.example.learingrealmandretrofit.card.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.CreateCardViewModel

class CreateCardViewModelFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCardViewModel::class.java)) {
            return CreateCardViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
