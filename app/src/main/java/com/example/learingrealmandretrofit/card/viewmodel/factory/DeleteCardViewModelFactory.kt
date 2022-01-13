package com.example.learingrealmandretrofit.card.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learingrealmandretrofit.card.viewmodel.DeleteCardViewModel

class DeleteCardViewModelFactory(private val application: Application, private val cardId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteCardViewModel::class.java)) {
            return DeleteCardViewModel(cardId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}