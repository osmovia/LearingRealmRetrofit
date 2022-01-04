package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.response.CardResponse
import com.example.learingrealmandretrofit.objects.response.SuccessResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsideDeckUpdateCardViewModel(private val token: String, private val deckId: Int) : ViewModel() {

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

        BaseApi.retrofitHeader(token).createCard(params = cardView)
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
            addCardToDeckRetrofit(cardId = card.id)
            realm.close()
        }, {
            _showSpinner.value = false
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    private fun addCardToDeckRetrofit(cardId: Int) {

        BaseApi.retrofitHeader(token).addCardToDeck(deckId = deckId, cardId = cardId)
            .enqueue(object : Callback<SuccessResponse?> {
            override fun onResponse(call: Call<SuccessResponse?>, response: Response<SuccessResponse?>) {
                if (response.isSuccessful) {
                    addCardToDeckRealm(cardId = cardId)
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }
            override fun onFailure(call: Call<SuccessResponse?>, t: Throwable) {
                _showToast.value = R.string.connection_issues
                _showSpinner.value = false
            }
        })
    }

    private fun addCardToDeckRealm(cardId: Int) {
//        val config = ConfigurationRealm.configuration
//        val realm = Realm.getInstance(config)
//        realm.executeTransactionAsync({ realmTransaction ->
//
//            val deck = realmTransaction
//                .where(Deck::class.java)
//                .equalTo("id", deckId)
//                .findFirst()
//                ?.cards
//
//            val card = realmTransaction
//                .where(Card::class.java)
//                .equalTo("id", cardId)
//                .findFirst()
//
//            deck?.add(card)
//
//        }, {
//            _success.value = true
//            _showSpinner.value = false
//            realm.close()
//        }, {
//            _showToast.value = R.string.problem_realm
//            _showSpinner.value = false
//            realm.close()
//        })
    }

    fun updateCardRetrofit(card: CardParameters) {

        if (card.example.isEmpty() || card.translation.isEmpty() || card.word.isEmpty()) {
            _showToast.value = R.string.empty_fields
            return
        }

        _showSpinner.value = true
        BaseApi.retrofitHeader(token).updateCard(id = card.id, params = card)
            .enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    updateCardRealm(responseBody.card)
                } else {
                    _showToast.value = response.code().toString()
                }
                _showSpinner.value = false
            }
            override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }

    private fun updateCardRealm(card: CardParameters) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val result = realmTransaction
                .where(Card::class.java)
                .equalTo("id", card.id)
                .findFirst()
            result?.example = card.example
            result?.translation = card.translation
            result?.word = card.word
        }, {
            _success.value = true
            realm.close()
        },{
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }
}
