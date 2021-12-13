package com.example.learingrealmandretrofit.deck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.DeckParameters
import com.example.learingrealmandretrofit.objects.response.DeckCreateOrUpdateResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteDeckViewModel : ViewModel() {

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

    fun deleteDeckRetrofit(deckId: Int) {
        _showSpinner.value = true
        BaseApi.retrofit.deleteDeck(id = deckId, token = token.value.orEmpty()).enqueue(object : Callback<DeckCreateOrUpdateResponse?> {
            override fun onResponse(call: Call<DeckCreateOrUpdateResponse?>, response: Response<DeckCreateOrUpdateResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    deleteDeckRealm(responseBody.deck)
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

    private fun deleteDeckRealm(deck: DeckParameters) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            val deleteDeck = realmTransaction
                .where(Deck::class.java)
                .equalTo("id", deck.id)
                .findFirst()
            deleteDeck?.deleteFromRealm()
        }, {
            _success.value = true
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }
}
