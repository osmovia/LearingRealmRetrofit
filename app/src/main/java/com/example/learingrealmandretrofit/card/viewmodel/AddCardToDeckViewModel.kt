package com.example.learingrealmandretrofit.card.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.response.SuccessResponse
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

    private val listSelectFlag = mutableListOf<Deck>()

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

    private fun addCardToDeckRealm(deckId: Int) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val currentDeck = realmTransaction
                .where(Deck::class.java)
                .equalTo("id", deckId)
                .findFirst()

            val currentCard = realmTransaction
                .where(Card::class.java)
                .equalTo("id", cardId)
                .findFirst()

            currentDeck?.cards?.add(currentCard)

        }, {
            addCardToDeckRetrofit()
            realm.close()
        }, {
            _showToast.value = false
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    fun addCardToDeckRetrofit() {
        _showSpinner.value = true

        if (listSelectFlag.size == 0) {
            _showSpinner.value = false
            _success.value = true
        }

        listSelectFlag.firstOrNull()?.let { currentDeck ->
            BaseApi.retrofit.addCardToDeck(token = token, cardId = cardId, deckId = currentDeck.id)
                .enqueue(object : Callback<SuccessResponse?> {
                override fun onResponse(call: Call<SuccessResponse?>, response: Response<SuccessResponse?>) {
                    if (response.isSuccessful) {
                        addCardToDeckRealm(currentDeck.id)
                        listSelectFlag.remove(currentDeck)
                    } else {
                        _showToast.value = response.code().toString()
                        _showSpinner.value = false
                    }
                }
                override fun onFailure(call: Call<SuccessResponse?>, t: Throwable) {
                    _showToast.value = R.string.connection_issues
                    _showSpinner.value = false
                }
            })
        }
    }

    fun changeStateCheckCheckbox(isChecked: Boolean, deck: Deck) {
        if (isChecked) {
            for (it in listSelectFlag) {
                if (it == deck) {
                    return
                }
            }
            listSelectFlag.add(deck)
        } else {
            for (it in listSelectFlag) {
                if (it == deck) {
                    listSelectFlag.remove(deck)
                    return
                }
            }
        }
    }

}
