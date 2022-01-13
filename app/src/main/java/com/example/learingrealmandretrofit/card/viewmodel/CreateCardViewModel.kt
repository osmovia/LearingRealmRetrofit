package com.example.learingrealmandretrofit.card.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.card.Card
import com.example.learingrealmandretrofit.objects.CardParameters
import com.example.learingrealmandretrofit.objects.response.CardResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateCardViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

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
        BaseApi.retrofit(context).createCard(params = cardView).enqueue(object : Callback<CardResponse?> {
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
            _showSpinner.value = false
            realm.close()
        }, {
            _showToast.value = R.string.problem_realm
            _showSpinner.value = false
            realm.close()
        })
    }
}
