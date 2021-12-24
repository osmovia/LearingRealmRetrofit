package com.example.learingrealmandretrofit.deck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.DeckParameters
import com.example.learingrealmandretrofit.objects.response.DeckListResponse
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.Sort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeckViewModel(private val token: String) : ViewModel() {

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _getAllDecksRealm = MutableLiveData<RealmResults<Deck>>()
    val gelAllDecksRealm: LiveData<RealmResults<Deck>>
        get() = _getAllDecksRealm

    init {
        pullDecks()
        getAllDecksRetrofit()
    }

    private fun getAllDecksRetrofit() {
        _showSpinner.value = true
        BaseApi.retrofit.getDecks(token = token).enqueue(object : Callback<DeckListResponse?> {
            override fun onResponse(call: Call<DeckListResponse?>, response: Response<DeckListResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    checkCurrentDecksInRealm(responseBody.decks)
                } else {
                    _showToast.value = response.code().toString()
                }
                _showSpinner.value = false
            }
            override fun onFailure(call: Call<DeckListResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }

    private fun checkCurrentDecksInRealm(decksList: List<DeckParameters>) {
//        val config = ConfigurationRealm.configuration
//        val realm = Realm.getInstance(config)
//        realm.executeTransactionAsync({ realmTransaction ->
//            for (deck in decksList) {
//                val newListCard = RealmList<Card>()
//                val currentListCard = deck.cards
//
//                currentListCard.forEach { card ->
//                    val cardRealm = Card(
//                        id = card.id,
//                        word = card.word,
//                        translation = card.translation,
//                        example = card.example
//                    )
//                    newListCard.add(cardRealm)
//                }
//
//                val coincideDeckId = realmTransaction
//                    .where(Deck::class.java)
//                    .equalTo("id", deck.id)
//                    .findFirst()
//                if (coincideDeckId == null) {
//                    val newDeck = Deck(id = deck.id, title = deck.title, cards = newListCard)
//                    realmTransaction.insert(newDeck)
//                }
//            }
//        }, {
//            realm.close()
//        }, {
//            _showToast.value = R.string.problem_realm
//            realm.close()
//        })
    }

    private fun pullDecks() {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        _getAllDecksRealm.value = realm.where(Deck::class.java).findAll().sort("title", Sort.ASCENDING)
    }
}
