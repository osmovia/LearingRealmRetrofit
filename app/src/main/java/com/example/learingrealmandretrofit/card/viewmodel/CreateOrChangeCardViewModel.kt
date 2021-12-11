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

class CreateOrChangeCardViewModel : ViewModel() {

    val token = MutableLiveData<String>()

    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    private val _translate = MutableLiveData<String>()
    val translate: LiveData<String>
        get() = _translate

    private val _example = MutableLiveData<String>()
    val example: LiveData<String>
        get() = _example

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
        _showSpinner.value = true
        BaseApi.retrofit.createCard(token = token.value.orEmpty(), params = cardView).enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    createCardRealm(responseBody.card)
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
    }

    fun updateCardRetrofit(card: CardParameters) {
            _showSpinner.value = true
            BaseApi.retrofit.updateCard(id = card.id, params = card , token = token.value.orEmpty()).enqueue(object : Callback<CardResponse?> {
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
