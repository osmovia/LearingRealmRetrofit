package com.example.learingrealmandretrofit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class ViewModel : ViewModel() {
    val dataAddNewWord: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}