package com.example.learingrealmandretrofit.authentication.sigin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.objects.SessionSharedPreferencesWrite
import com.example.learingrealmandretrofit.objects.request.SessionRequest
import com.example.learingrealmandretrofit.objects.request.SignInRequest
import com.example.learingrealmandretrofit.objects.request.UserSignInRequest
import com.example.learingrealmandretrofit.objects.response.AuthenticationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean>
        get() = _showSpinner

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    private val _saveSession = MutableLiveData<SessionSharedPreferencesWrite>()
    val saveSession: LiveData<SessionSharedPreferencesWrite>
        get() = _saveSession

    fun entryApp(emailView: String, passwordView: String) {
        _showSpinner.value = true
        val user = UserSignInRequest(email = emailView, password = passwordView)
        val request = SignInRequest(session = SessionRequest(), user = user)
        BaseApi.retrofit(null).signIn(request).enqueue(object : Callback<AuthenticationResponse?> {
            override fun onResponse(call: Call<AuthenticationResponse?>, response: Response<AuthenticationResponse?>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _saveSession.value = SessionSharedPreferencesWrite(
                        email = responseBody.user.email,
                        sessionToken = responseBody.session.token,
                        userId = responseBody.session.userId
                    )
                    _success.value = true
                } else {
                    _showToast.value = response.code().toString()
                }
                _showSpinner.value = false
            }
            override fun onFailure(call: Call<AuthenticationResponse?>, t: Throwable) {
                _showSpinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }
}
