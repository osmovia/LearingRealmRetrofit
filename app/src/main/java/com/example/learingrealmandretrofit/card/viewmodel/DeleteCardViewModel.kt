package com.example.learingrealmandretrofit.card.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.objects.CardDeck
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteCardViewModel(private val cardId: Int, application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    fun removeCardRetrofit() {
        _showSpinner.value = true
        BaseApi.retrofit(context).deleteCard(id = cardId).enqueue(object : Callback<CardResponse?> {
            override fun onResponse(call: Call<CardResponse?>, response: Response<CardResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    removeCardRealm(responseBody.card.id)
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

    private fun removeCardRealm(cardId: Int) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val result = realmTransaction
                .where(Card::class.java)
                .equalTo("id", cardId)
                .findFirst()
            result?.deleteFromRealm()

            val cardDeck = realmTransaction
                .where(CardDeck::class.java)
                .equalTo("cardId", cardId)
                .findAll()
            cardDeck.deleteAllFromRealm()

        }, {
            _showSpinner.value = false
            _success.value = true
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            _showSpinner.value = false
            realm.close()
        })
    }
}
