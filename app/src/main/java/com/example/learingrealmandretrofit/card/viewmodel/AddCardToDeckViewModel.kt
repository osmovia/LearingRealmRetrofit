package com.example.learingrealmandretrofit.card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.CardDeck
import com.example.learingrealmandretrofit.objects.request.AddCardToDecksRequest
import com.example.learingrealmandretrofit.objects.response.AddCardToDecksResponse
import com.example.learingrealmandretrofit.objects.response.CardDeckIdResponse
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCardToDeckViewModel(private val token: String, private val cardId: Int) : ViewModel() {

    private val _getAllDecksRealm = MutableLiveData<RealmResults<Deck>>()
    val gelAllDecksRealm: LiveData<RealmResults<Deck>>
        get() = _getAllDecksRealm

    private val listSelectFlag = mutableListOf<Int>()

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    init {
        pullDecks()
    }

    private fun pullDecks() {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        _getAllDecksRealm.value = realm.where(Deck::class.java).findAll().sort("title", Sort.ASCENDING)
    }

    private fun addAndRemoveCardFromDecksRealm(response: AddCardToDecksResponse?) {

        val listDeleteCardDecks = response?.deletedCardDecks ?: return
        val listCreateCardDecks = response.createdCardDecks

        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->

            listCreateCardDecks.forEach { cardDeck ->
                val cardDeckRealm = CardDeck(
                    id = cardDeck.id,
                    cardId = cardDeck.cardId,
                    deckId = cardDeck.deckId
                )
                realmTransaction.insert(cardDeckRealm)
            }

            listDeleteCardDecks.forEach { cardDeck ->
                val deleteCardDeck = realmTransaction.where(CardDeck::class.java).equalTo("id", cardDeck.id).findFirst()
                deleteCardDeck?.deleteFromRealm()
            }
        }, {
            addCardDeckInCardsAndDecks(response.createdCardDecks)
            realm.close()
        }, {
            _showSpinner.value = false
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    private fun addCardDeckInCardsAndDecks(cardDecks: List<CardDeckIdResponse>) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->

            cardDecks.forEach { itemCardDeck ->
                val cardDeck = realmTransaction
                    .where(CardDeck::class.java)
                    .equalTo("id", itemCardDeck.id)
                    .findFirst()

                val deck = realmTransaction
                    .where(Deck::class.java)
                    .equalTo("id", itemCardDeck.deckId)
                    .findFirst()
                deck?.cardDecks?.add(cardDeck)

                val card = realmTransaction
                    .where(Card::class.java)
                    .equalTo("id", itemCardDeck.cardId)
                    .findFirst()
                card?.cardDecks?.add(cardDeck)
            }
        }, {
            _success.value = true
            _showSpinner.value = false
            realm.close()
        }, {
            _showSpinner.value = false
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    fun addAndRemoveCardFromDecksRetrofit() {
        _showSpinner.value = true
        val request = AddCardToDecksRequest(deckIds = listSelectFlag)
        BaseApi.retrofitHeader(token).addCardFromDecks(params = request, id = cardId).enqueue(object : Callback<AddCardToDecksResponse?> {
            override fun onResponse(call: Call<AddCardToDecksResponse?>, response: Response<AddCardToDecksResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    addAndRemoveCardFromDecksRealm(responseBody)
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }
            override fun onFailure(call: Call<AddCardToDecksResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }

    fun changeStateCheckCheckbox(isChecked: Boolean, deck: Deck) {
        if (isChecked) {
            for (it in listSelectFlag) {
                if (it == deck.id) {
                    return
                }
            }
            listSelectFlag.add(deck.id)
        } else {
            for (it in listSelectFlag) {
                if (it == deck.id) {
                    listSelectFlag.remove(deck.id)
                    return
                }
            }
        }
    }
}
