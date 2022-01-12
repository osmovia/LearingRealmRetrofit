package com.example.learingrealmandretrofit.deck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.CardDeck
import com.example.learingrealmandretrofit.objects.response.CardResponse
import com.example.learingrealmandretrofit.objects.response.DeckDeletedWitchCardsResponse
import com.example.learingrealmandretrofit.objects.response.DeckResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteDeckViewModel(private val token: String, private val deckId: Int) : ViewModel() {

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    fun deleteDeckRetrofit() {
        _showSpinner.value = true
        BaseApi.retrofitHeader(token).deleteDeck(id = deckId).enqueue(object : Callback<DeckResponse?> {
            override fun onResponse(call: Call<DeckResponse?>, response: Response<DeckResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    deleteOnlyDeckRealm()
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }
            override fun onFailure(call: Call<DeckResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }

    fun deleteDeckWithCardsRetrofit() {
        _showSpinner.value = true
        BaseApi.retrofitHeader(token).deleteDeckWitchCards(deckId).enqueue(object : Callback<DeckDeletedWitchCardsResponse?> {
            override fun onResponse(
                call: Call<DeckDeletedWitchCardsResponse?>,
                response: Response<DeckDeletedWitchCardsResponse?>
            ) {
                if (response.isSuccessful) {
                    deleteDeckWithCards()
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }
            override fun onFailure(call: Call<DeckDeletedWitchCardsResponse?>, t: Throwable) {
                _showToast.value = R.string.connection_issues
                _showSpinner.value = false
            }
        })
    }

    private fun deleteDeckWithCards() {
        val realm = Realm.getInstance(ConfigurationRealm.configuration)
        realm.executeTransactionAsync({ realmTransaction ->

            val deck = realmTransaction
                .where(Deck::class.java)
                .equalTo("id", deckId)
                .findFirst()

            val cardDecks = realmTransaction
                .where(CardDeck::class.java)
                .equalTo("deckId", deckId)
                .findAll()

            cardDecks?.forEach { cardDeck ->
                val card = realmTransaction
                    .where(Card::class.java)
                    .equalTo("id", cardDeck.cardId)
                    .findFirst()
                card?.deleteFromRealm()

                val result = realmTransaction
                    .where(CardDeck::class.java)
                    .equalTo("cardId", cardDeck.cardId)
                    .findAll()
                result?.deleteAllFromRealm()

            }
            cardDecks?.deleteAllFromRealm()
            deck?.deleteFromRealm()

        }, {
            _showSpinner.value = false
            _success.value = true
           realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            _showSpinner.value = false
            realm.close()
        })
    }

    private fun deleteOnlyDeckRealm() {
        val realm = Realm.getInstance(ConfigurationRealm.configuration)
        realm.executeTransactionAsync({ realmTransaction ->

            val deck = realmTransaction
                .where(Deck::class.java)
                .equalTo("id", deckId)
                .findFirst()
            deck?.deleteFromRealm()

            val cardDecks = realmTransaction
                .where(CardDeck::class.java)
                .equalTo("deckId", deckId)
                .findAll()
            cardDecks?.deleteAllFromRealm()

        }, {
            _showSpinner.value = false
            _success.value = true
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            _showSpinner.value = false
            realm.close()
        })

    }
}
