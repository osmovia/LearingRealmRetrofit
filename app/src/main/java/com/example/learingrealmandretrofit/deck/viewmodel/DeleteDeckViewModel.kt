package com.example.learingrealmandretrofit.deck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.response.CardResponse
import com.example.learingrealmandretrofit.objects.response.DeckGetOrCreateOrUpdateResponse
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

    private val listCardId = mutableListOf<Int>()


    fun deleteDeckRetrofit(withCards: Boolean) {
        _showSpinner.value = true
        BaseApi.retrofit.deleteDeck(id = deckId, token = token).enqueue(object : Callback<DeckGetOrCreateOrUpdateResponse?> {
            override fun onResponse(call: Call<DeckGetOrCreateOrUpdateResponse?>, response: Response<DeckGetOrCreateOrUpdateResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (withCards) {
                        pullCardsForDelete()
                    } else {
                        deleteOnlyDeckRealm()
                    }
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }
            override fun onFailure(call: Call<DeckGetOrCreateOrUpdateResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }

    private fun pullCardsForDelete() {
//        val config = ConfigurationRealm.configuration
//        val realm = Realm.getInstance(config)
//        realm.executeTransactionAsync({ realmTransaction ->
//
//            val allCards = realmTransaction
//                .where(Deck::class.java)
//                .equalTo("id", deckId)
//                .findFirst()
//                ?.cards
//
//            allCards?.forEach { card ->
//                listCardId.add(card.id)
//            }
//        }, {
//            deleteCardsRetrofit()
//            realm.close()
//        }, {
//            _showSpinner.value = false
//            _showToast.value = R.string.problem_realm
//            realm.close()
//        })
    }

    private fun deleteCardsRetrofit() {

        if (listCardId.size == 0) {
            deleteOnlyDeckRealm()
        }

        listCardId.firstOrNull()?.let { cardId ->
            BaseApi.retrofit.deleteCard(token = token, cardId)
                .enqueue(object : Callback<CardResponse?> {
                    override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                        val responseBody = response.body()
                        if (response.isSuccessful && responseBody != null) {
                            deleteOneCardRealm(responseBody.card.id)
                        } else {
                            _showSpinner.value = false
                            _showToast.value = response.code().toString()
                        }
                    }

                    override fun onFailure(call: Call<CardResponse?>, t: Throwable) {
                        _showToast.value = R.string.connection_issues
                        _showSpinner.value = false
                    }
                })
        }
    }

    private fun deleteOneCardRealm(cardId: Int) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val deleteCard = realmTransaction
                .where(Card::class.java)
                .equalTo("id", cardId)
                .findFirst()
            deleteCard?.deleteFromRealm()
        }, {
            listCardId.remove(cardId)
            deleteCardsRetrofit()
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            _showSpinner.value = false
            realm.close()
        })
    }

    private fun deleteOnlyDeckRealm() {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val deleteDeck = realmTransaction
                .where(Deck::class.java)
                .equalTo("id", deckId)
                .findFirst()
            deleteDeck?.deleteFromRealm()
        }, {
            _success.value = true
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
        })
        _showSpinner.value = false
    }
}
