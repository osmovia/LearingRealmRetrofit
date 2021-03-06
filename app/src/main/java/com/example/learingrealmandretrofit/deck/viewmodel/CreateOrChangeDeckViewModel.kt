package com.example.learingrealmandretrofit.deck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.Constants
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.deck.Deck
import com.example.learingrealmandretrofit.objects.DeckParameters
import com.example.learingrealmandretrofit.objects.request.DeckCreateOrUpdateRequest
import com.example.learingrealmandretrofit.objects.request.DeckTitleRequest
import com.example.learingrealmandretrofit.objects.response.DeckResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateOrChangeDeckViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<String>()
    val success: LiveData<String>
        get() = _success

    private val _currentDeck = MutableLiveData<Deck>()
    val currentDeck: LiveData<Deck>
        get() = _currentDeck

    private val _newTitle = MutableLiveData<String>()
    val newTitle: LiveData<String>
        get() = _newTitle

    fun createDeck(title: String) {
        if (title.isEmpty()) {
            _showToast.value = R.string.empty_title
            return
        }
        _showSpinner.value = true
        val titleRequest = DeckTitleRequest(title)
        val request = DeckCreateOrUpdateRequest(titleRequest)
        BaseApi.retrofit(context).createdDeck(params = request).enqueue(object : Callback<DeckResponse?> {
            override fun onResponse(call: Call<DeckResponse?>, response: Response<DeckResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    createDeckRealm(responseBody.deck)
                } else {
                    _showToast.value = response.code().toString()
                }
                _showSpinner.value = false
            }
            override fun onFailure(call: Call<DeckResponse?>, t: Throwable) {
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
            _success.value = Constants.CREATE
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    fun updateDeck(titleDeck: String) {
        if (titleDeck.isEmpty()) {
            _showToast.value = R.string.empty_title
            return
        }
        _showSpinner.value = true
        val requestTitle = DeckTitleRequest(titleDeck)
        val request = DeckCreateOrUpdateRequest(requestTitle)
        BaseApi.retrofit(context).updateDeck(id = _currentDeck.value?.id ?: return , params = request)
            .enqueue(object : Callback<DeckResponse?> {
            override fun onResponse(call: Call<DeckResponse?>, response: Response<DeckResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    updateDeckRealm(responseBody.deck)
                } else {
                    _showToast.value = response.code().toString()
                }
            }
            override fun onFailure(call: Call<DeckResponse?>, t: Throwable) {
                _showToast.value = R.string.connection_issues
                _showSpinner.value = false
            }
        })
    }

    private fun updateDeckRealm(deck: DeckParameters) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        _newTitle.value = deck.title
        realm.executeTransactionAsync({ realmTransaction ->
            val result = realmTransaction
                .where(Deck::class.java)
                .equalTo("id", deck.id)
                .findFirst()
            result?.title = deck.title
        }, {
            _success.value = Constants.UPDATE
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            realm.close()
        })
    }

    fun pullDeck(deckId: Int) {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        _currentDeck.value = realm.where(Deck::class.java).equalTo("id", deckId).findFirst()
    }
}
