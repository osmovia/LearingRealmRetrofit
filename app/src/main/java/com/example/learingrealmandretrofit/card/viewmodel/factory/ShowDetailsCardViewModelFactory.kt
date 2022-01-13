package com.example.learingrealmandretrofit.card.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.ShowDetailsCardViewModel

class ShowDetailsCardViewModelFactory(private val application: Application, private val cardId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowDetailsCardViewModel::class.java)) {
            return ShowDetailsCardViewModel(application, cardId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
