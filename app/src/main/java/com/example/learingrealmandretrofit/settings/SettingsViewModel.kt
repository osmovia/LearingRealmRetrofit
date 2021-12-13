package com.example.learingrealmandretrofit.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.ConfigurationRealm
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.objects.response.SuccessResponse
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsViewModel : ViewModel() {

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    fun signOutRetrofit(token: String) {
        _showSpinner.value = true
        BaseApi.retrofit.signOut(token, token).enqueue(object : Callback<SuccessResponse?> {
            override fun onResponse(call: Call<SuccessResponse?>, response: Response<SuccessResponse?>) {
                if (response.isSuccessful) {
                    deleteAllRealm()
                } else {
                    Log.d("response trouble", "Code: ${response.code()} \n Message: ${response.message()}")
                    _showToast.value = response.code().toString()
                }
                _showSpinner.value = false
            }
            override fun onFailure(call: Call<SuccessResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }

    private fun deleteAllRealm() {
        val config = ConfigurationRealm.configuration
        val realm = Realm.getInstance(config)
        realm.executeTransactionAsync({ realmTransaction ->
            realmTransaction.deleteAll()
        }, {
            _success.value = true
            realm.close()
        }, {
            realm.close()
            _showToast.value = R.string.connection_issues
        })
    }
}
