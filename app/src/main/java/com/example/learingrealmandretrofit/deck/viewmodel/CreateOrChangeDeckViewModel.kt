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
import com.example.learingrealmandretrofit.objects.request.DeckCreateOrUpdateRequest
import com.example.learingrealmandretrofit.objects.request.DeckTitleRequest
import com.example.learingrealmandretrofit.objects.response.DeckCreateOrUpdateResponse
import io.realm.Realm
import io.realm.RealmList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateOrChangeDeckViewModel : ViewModel() {

    val token = MutableLiveData<String>()

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    fun createDeck(title: String) {
        if (title.isEmpty()) {
            _showToast.value = R.string.empty_title
            return
        }
        _showSpinner.value = true
        val titleRequest = DeckTitleRequest(title)
        val request = DeckCreateOrUpdateRequest(titleRequest)
        BaseApi.retrofit.createdDeck(token = token.value.orEmpty(), params = request).enqueue(object : Callback<DeckCreateOrUpdateResponse?> {
            override fun onResponse(call: Call<DeckCreateOrUpdateResponse?>, response: Response<DeckCreateOrUpdateResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    createDeckRealm(responseBody.deck)
                } else {
                    _showToast.value = response.code().toString()
                }
                _showSpinner.value = false
            }
            override fun onFailure(call: Call<DeckCreateOrUpdateResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }

    private fun createDeckRealm(deck: DeckParameters) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val deckRealm = Deck(
                id = deck.id,
                title = deck.title
            )
            realmTransaction.insert(deckRealm)
        }, {
            _success.value = true
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    fun updateDeck(deck: Deck) {
        if (deck.title.isEmpty()) {
            _showToast.value = R.string.empty_title
            return
        }
        _showSpinner.value = true
        val requestTitle = DeckTitleRequest(deck.title)
        val request = DeckCreateOrUpdateRequest(requestTitle)
        BaseApi.retrofit.updateDeck(token = token.value.orEmpty(), id = deck.id, params = request)
            .enqueue(object : Callback<DeckCreateOrUpdateResponse?> {
            override fun onResponse(call: Call<DeckCreateOrUpdateResponse?>, response: Response<DeckCreateOrUpdateResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    updateDeckRealm(responseBody.deck)
                } else {
                    _showToast.value = response.code().toString()
                }
                _showSpinner.value = false
            }
            override fun onFailure(call: Call<DeckCreateOrUpdateResponse?>, t: Throwable) {
                _showToast.value = R.string.connection_issues
                _showSpinner.value = false
            }
        })
    }

    private fun updateDeckRealm(deck: DeckParameters) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val listRealm = RealmList<Card>()
            for (card in deck.cards) {
                val cardRealm = Card(
                    id = card.id,
                    word = card.word,
                    translation = card.translation,
                    example = card.example
                )
                listRealm.add(cardRealm)
            }
            val result = realmTransaction
                .where(Deck::class.java)
                .equalTo("id", deck.id)
                .findFirst()
            result?.title = deck.title
            result?.cards = listRealm
        }, {
            _success.value = true
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }
}
