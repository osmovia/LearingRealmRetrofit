package com.example.learingrealmandretrofit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learingrealmandretrofit.model.Post
import com.example.learingrealmandretrofit.repository.Repository
import kotlinx.coroutines.launch

class ViewModel(private val repository: Repository) : ViewModel() {
    val myResponse : MutableLiveData<Post> = MutableLiveData()

    fun getPost() {
        viewModelScope.launch {
            val response = repository.getPost()
            myResponse.value = response
        }
    }
}