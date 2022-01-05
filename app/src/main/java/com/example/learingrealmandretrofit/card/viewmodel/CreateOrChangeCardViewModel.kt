package com.example.learingrealmandretrofit.card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateOrChangeCardViewModel(private val token: String) : ViewModel() {

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
        BaseApi.retrofitHeader(token).createCard(params = cardView).enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    createCardRealm(responseBody.card)
                } else {
                    _showToast.value = response.code().toString()
                    _showSpinner.value = false
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
            _success.value = true
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
        })
        _showSpinner.value = false
    }

    fun updateCardRetrofit(cardView: CardParameters) {

        if (cardView.example.isEmpty() || cardView.translation.isEmpty() || cardView.word.isEmpty()) {
            _showToast.value = R.string.empty_fields
            return
        }

            _showSpinner.value = true
            BaseApi.retrofitHeader(token).updateCard(id = cardView.id, params = cardView).enqueue(object : Callback<CardResponse?> {
                override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        updateCardRealm(responseBody.card)
                    } else {
                        _showToast.value = response.code().toString()
                        _showSpinner.value = false
                    }
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
        _showSpinner.value = false
    }
}
