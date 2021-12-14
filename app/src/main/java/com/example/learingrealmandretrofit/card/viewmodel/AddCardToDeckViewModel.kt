package com.example.learingrealmandretrofit.card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.deck.Deck
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class AddCardToDeckViewModel : ViewModel() {

    private val _getAllDecksRealm = MutableLiveData<RealmResults<Deck>>()
    val gelAllDecksRealm: LiveData<RealmResults<Deck>>
        get() = _getAllDecksRealm

    init {
        pullDecks()
    }

    private fun pullDecks() {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        _getAllDecksRealm.value = realm.where(Deck::class.java).findAll().sort("title", Sort.ASCENDING)
    }
}
