package com.example.learingrealmandretrofit.deck.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.deck.viewmodel.CreateOrChangeDeckViewModel

class CreateOrChangeDeckViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateOrChangeDeckViewModel::class.java)) {
            return CreateOrChangeDeckViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}