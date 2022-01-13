package com.example.learingrealmandretrofit.card.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.AddCardToDeckViewModel

class AddCardToDeckViewModelFactory(private val application: Application, private val cardId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCardToDeckViewModel::class.java)) {
            return AddCardToDeckViewModel(application, cardId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}