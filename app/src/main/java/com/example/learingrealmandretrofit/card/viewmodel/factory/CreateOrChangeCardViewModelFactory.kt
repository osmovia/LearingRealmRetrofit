package com.example.learingrealmandretrofit.card.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.CreateOrChangeCardViewModel

class CreateOrChangeCardViewModelFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateOrChangeCardViewModel::class.java)) {
            return CreateOrChangeCardViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}