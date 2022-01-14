package com.example.learingrealmandretrofit.card.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsCardViewModel(application: Application, private val cardId: Int) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val _showDetailsCard = MutableLiveData<CardParameters>()
    val showDetailsCard: LiveData<CardParameters>
        get() = _showDetailsCard

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _stateView = MutableLiveData<Boolean>()
    val stateView: LiveData<Boolean>
        get() = _stateView

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    private val _showAllDecksCard = MutableLiveData<MutableList<String>>()
    val showAllDecksCard: LiveData<MutableList<String>>
        get() = _showAllDecksCard


    init {
        getCard()
        _stateView.value = false
    }

    fun changeState(state: Boolean) {
        _stateView.value = state
    }

    fun changeCard(cardView: CardParameters) {
        if (cardView.example.isEmpty() || cardView.translation.isEmpty() || cardView.word.isEmpty()) {
            _showToast.value = R.string.empty_fields
            return
        }
        _showSpinner.value = true

        BaseApi.retrofit(context).updateCard(cardId, cardView).enqueue(object : Callback<CardResponse?> {
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
            _showDetailsCard.value = card
            _stateView.value = false
            _showSpinner.value = false
            realm.close()
        },{
            _showSpinner.value = false
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    private fun getCard() {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val card = realmTransaction
                .where(Card::class.java)
                .equalTo("id", cardId)
                .findFirst()
            if (card != null) {
                _showDetailsCard.postValue(
                    CardParameters(
                        id = card.id,
                        word = card.word,
                        example = card.example,
                        translation = card.translation
                    )
                )
            }
        }, {
            _showSpinner.value = false
            realm.close()
        }, {
            _showSpinner.value = false
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }
}
