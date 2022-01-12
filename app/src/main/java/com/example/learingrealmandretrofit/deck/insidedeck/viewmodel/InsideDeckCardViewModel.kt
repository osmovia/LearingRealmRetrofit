package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.CardDeck
import com.example.learingrealmandretrofit.objects.CardDeckParameters
import com.example.learingrealmandretrofit.objects.response.CardDeckListResponse
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsideDeckCardViewModel(private val token: String, private val deckId: Int) : ViewModel() {

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _getAllCardsRealm = MutableLiveData<RealmResults<CardDeck>>()
    val getAllCardsRealm: LiveData<RealmResults<CardDeck>>
        get() = _getAllCardsRealm

    init {
        getCardsInsideDeck()
    }

    private fun getAllCardDeck() {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        _getAllCardsRealm.value = realm
            .where(CardDeck::class.java)
            .equalTo("deckId", deckId)
            .findAll()
        _showSpinner.value = false
    }

    private fun getCardsInsideDeck() {
        _showSpinner.value = true
        BaseApi.retrofitHeader(token).getCardDecks().enqueue(object : Callback<CardDeckListResponse?> {
            override fun onResponse(
                call: Call<CardDeckListResponse?>,
                response: Response<CardDeckListResponse?>
            ) {
                val responseBode = response.body()
                if (response.isSuccessful && responseBode != null) {
                    checkCurrentCardsInRealm(responseBode.cardDecks)
                } else {
                    getAllCardDeck()
                    _showToast.value = response.code().toString()
                }
            }

            override fun onFailure(call: Call<CardDeckListResponse?>, t: Throwable) {
                getAllCardDeck()
                _showToast.value = R.string.connection_issues
            }
        })
    }

    private fun checkCurrentCardsInRealm(cards: List<CardDeckParameters>) {
        val createCardDeckId = mutableListOf<Int>()

        val realm = Realm.getInstance(ConfigurationRealm.configuration)
        realm.executeTransactionAsync({ realmTransaction ->

            cards.forEach { cardDeckParameters ->
                val currentCardDeck = realmTransaction
                    .where(CardDeck::class.java)
                    .equalTo("id", cardDeckParameters.id)
                    .findFirst()

                if (currentCardDeck == null) {
                    createCardDeckId.add(cardDeckParameters.id)
                    realmTransaction.insert(
                        CardDeck(
                            id = cardDeckParameters.id,
                            deckId = cardDeckParameters.deckId,
                            cardId = cardDeckParameters.cardId
                        )
                    )
                }
            }
        }, {
            realm.close()
            if (createCardDeckId.size == 0) {
                getAllCardDeck()
            } else {
                addCardDeckInCardsAndDecks(createCardDeckId)
            }
        }, {
            realm.close()
            _showSpinner.value = false
            _showToast.value = R.string.problem_realm
        })
    }

    private fun addCardDeckInCardsAndDecks(createCardDeckId: MutableList<Int>) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->

            createCardDeckId.forEach { cardDeckId ->
                val cardDeckResult = realmTransaction
                    .where(CardDeck::class.java)
                    .equalTo("id", cardDeckId)
                    .findFirst()

                val deck = realmTransaction
                    .where(Deck::class.java)
                    .equalTo("id", cardDeckResult?.deckId)
                    .findFirst()
                deck?.cardDecks?.add(cardDeckResult)

                val card = realmTransaction
                    .where(Card::class.java)
                    .equalTo("id", cardDeckResult?.cardId)
                    .findFirst()
                card?.cardDecks?.add(cardDeckResult)
            }
        }, {
            realm.close()
            getAllCardDeck()
        }, {
            _showSpinner.value = false
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }
}
