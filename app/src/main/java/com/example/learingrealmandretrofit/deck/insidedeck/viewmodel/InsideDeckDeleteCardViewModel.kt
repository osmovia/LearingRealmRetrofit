package com.example.learingrealmandretrofit.deck.insidedeck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.objects.CardDeck
import com.example.learingrealmandretrofit.objects.response.CardDeckResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsideDeckDeleteCardViewModel(private val token: String, private val deckId: Int, private val cardId: Int, private val cardDeckId: Int) : ViewModel() {

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    fun deleteCardRetrofit() {
        _showSpinner.value = true
        BaseApi.retrofit.deleteCardDeck(token = token, id = cardDeckId).enqueue(object : Callback<CardDeckResponse?> {
            override fun onResponse(call: Call<CardDeckResponse?>, response: Response<CardDeckResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    deleteCardRealm(responseBody.cardDeck.id)
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }

            override fun onFailure(call: Call<CardDeckResponse?>, t: Throwable) {
                _showToast.value = R.string.connection_issues
                _showSpinner.value = false
            }
        })
    }

    private fun deleteCardRealm(cardDeckId: Int) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val card = realmTransaction
                .where(CardDeck::class.java)
                .equalTo("id", cardDeckId)
                .findFirst()
            card?.deleteFromRealm()
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
}
