package com.example.learingrealmandretrofit.card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.response.CardListResponse
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardViewModel(private val token: String) : ViewModel() {

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _getAllCardsRealm = MutableLiveData<RealmResults<Card>>()
    val getAllCardsRealm: LiveData<RealmResults<Card>>
        get() = _getAllCardsRealm

    init {
        pullCardsRealm()
        getAllCardRetrofit()
    }

    private fun getAllCardRetrofit() {
        _showSpinner.value = true

        BaseApi.retrofit.getCards(token = token).enqueue(object : Callback<CardListResponse?> {
            override fun onResponse(call: Call<CardListResponse?>, response: Response<CardListResponse?>) {
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    checkCurrentCardsInRealm(responseBody.cards)
                } else {
                    _showSpinner.value = false
                    _showToast.value = response.code().toString()
                }
            }

            override fun onFailure(call: Call<CardListResponse?>, t: Throwable) {
                _showToast.value = R.string.connection_issues
                _showSpinner.value = false
            }
        })
    }

    private fun checkCurrentCardsInRealm(listCards: List<CardParameters>) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync ({ realmTransaction ->
            for (item in listCards) {
                val coincideCardId = realmTransaction
                    .where(Card::class.java)
                    .equalTo("id", item.id)
                    .findFirst()
                if (coincideCardId == null) {
                    val cardRealm = Card(
                        id = item.id,
                        word = item.word,
                        translation = item.translation,
                        example = item.example
                    )
                    realmTransaction.insert(cardRealm)
                }
            }
        }, {
            realm.close()
            _showSpinner.value = false
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
            _showSpinner.value = false
        })
    }

    private fun pullCardsRealm() {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        _getAllCardsRealm.value =  realm.where(Card::class.java).findAll().sort("word", Sort.ASCENDING)
    }
}
