package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.CardDeck
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.request.CardDeckRequest
import com.example.learingrealmandretrofit.objects.request.CardDeckMainRequest
import com.example.learingrealmandretrofit.objects.response.CardDeckMainResponse
import com.example.learingrealmandretrofit.objects.response.CardDeckResponse
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsideDeckCreateCardViewModel(application: Application, private val deckId: Int) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    fun createCardRetrofit(cardView: CardParameters) {

        if (cardView.example.isEmpty() || cardView.translation.isEmpty() || cardView.word.isEmpty()) {
            _showToast.value = R.string.empty_fields
            return
        }

        _showSpinner.value = true

        BaseApi.retrofit(context).createCard(params = cardView)
            .enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    createCardRealm(responseBody.card)
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }
            override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }

    private fun createCardRealm(card: CardParameters) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val cardRealm = Card(
                id = card.id,
                word = card.word,
                example = card.example,
                translation = card.translation
            )
            realmTransaction.insert(cardRealm)
        }, {
            createCardDeckRetrofit(cardId = card.id)
            realm.close()
        }, {
            _showSpinner.value = false
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    private fun createCardDeckRetrofit(cardId: Int) {
        BaseApi.retrofit(context).createCardDeck(CardDeckMainRequest(CardDeckRequest(cardId = cardId, deckId = deckId)))
            .enqueue(object : Callback<CardDeckMainResponse?> {
            override fun onResponse(call: Call<CardDeckMainResponse?>, response: Response<CardDeckMainResponse?>) {

                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    createCardDeckRealm(responseBody.cardDeck)
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }
            override fun onFailure(call: Call<CardDeckMainResponse?>, t: Throwable) {
                _showToast.value = R.string.connection_issues
                _showSpinner.value = false
            }
        })
    }

    private fun createCardDeckRealm(response: CardDeckResponse) {
        val realm = Realm.getInstance(ConfigurationRealm.configuration)
        realm.executeTransactionAsync({ realmTransaction ->

            realmTransaction.insert(CardDeck(
                id = response.id,
                cardId = response.cardId,
                deckId = response.deckId
            ))

            val cardDeck = realmTransaction
                .where(CardDeck::class.java)
                .equalTo("id", response.id)
                .findFirst()

            val card = realmTransaction
                .where(Card::class.java)
                .equalTo("id", response.cardId)
                .findFirst()
            card?.cardDecks?.add(cardDeck)

            val deck = realmTransaction
                .where(Deck::class.java)
                .equalTo("id", response.deckId)
                .findFirst()
            deck?.cardDecks?.add(cardDeck)

        }, {
            _success.value = true
            _showSpinner.value = false
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            _showSpinner.value = false
            realm.close()
        })
    }
}
