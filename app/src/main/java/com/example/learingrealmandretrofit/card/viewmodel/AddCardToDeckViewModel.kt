package com.example.learingrealmandretrofit.card.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.*
import com.example.learingrealmandretrofit.objects.request.AddCardToDecksRequest
import com.example.learingrealmandretrofit.objects.response.AddCardToDecksResponse
import io.realm.Realm
import io.realm.RealmList
import io.realm.Sort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AddCardToDeckViewModel(application: Application, private val cardId: Int) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val _decks = RealmList<Deck>()
    val deck: RealmList<Deck>
        get() = _decks

    private val _decksIds = mutableListOf<Int>()
    val decksIds: MutableList<Int>
        get() = _decksIds

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    private val _listsReady = MutableLiveData<Boolean>()
    val listsReady: LiveData<Boolean>
        get() = _listsReady

    private val _updateRecycler = MutableLiveData<Int>()
    val updateRecycler: LiveData<Int>
        get() = _updateRecycler

    init {
        pullDecks()
    }

    private fun pullDecks() {
        val realm = Realm.getInstance(ConfigurationRealm.configuration)

        realm.executeTransactionAsync({ realmTransaction ->
            val decks = realmTransaction
                .where(Deck::class.java)
                .findAll()
                .sort("title", Sort.ASCENDING)

            _decks.addAll(realmTransaction.copyFromRealm(decks))

            run firstLoop@{ _decks.forEach { deck ->
                run secondLoop@{ deck.cardDecks.forEach { cardDeck ->
                    if (cardDeck.cardId == cardId) {
                        decksIds.add(cardDeck.deckId)
                        return@secondLoop
                    }
                }
                }
            }
            }

        }, {
            _listsReady.value = true
            realm.close()
        }, {
            realm.close()
        })
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
                val result = realmTransaction
                    .where(CardDeck::class.java)
                    .equalTo("id", cardDeck.id)
                    .findFirst()
                result?.deleteFromRealm()
            }

            response.createdCardDecks.forEach { itemCardDeck ->
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
        val request = AddCardToDecksRequest(deckIds = _decksIds)

        BaseApi
            .retrofit(context)
            .updateDecksForCardResponse(params = request, id = cardId)
            .enqueue(object : Callback<AddCardToDecksResponse?> {

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

    fun changeStateCheckCheckbox(deck: Deck, position: Int) {
        if (_decksIds.contains(deck.id)) {
            _decksIds.remove(deck.id)
        } else {
            _decksIds.add(deck.id)
        }
        _updateRecycler.value = position
    }
}
