package com.example.learingrealmandretrofit.authentication.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.objects.SessionSharedPreferencesWrite
import com.example.learingrealmandretrofit.objects.request.SessionRequest
import com.example.learingrealmandretrofit.objects.request.SignUpRequest
import com.example.learingrealmandretrofit.objects.request.UserSignUpRequest
import com.example.learingrealmandretrofit.objects.response.AuthenticationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val email = MutableLiveData<String>()
    private val password = MutableLiveData<String>()
    private val passwordConfirmation = MutableLiveData<String>()
    private val fieldCorrect = MutableLiveData<Boolean>()
    val inputLayoutEmail = MutableLiveData<Int?>()
    val inputLayoutPassword = MutableLiveData<Int?>()
    val inputLayoutPasswordConfirmation = MutableLiveData<Int?>()

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _showToast = MutableLiveData<Any>()
    val showToast: LiveData<Any>
        get() = _showToast

    private val _saveSession = MutableLiveData<SessionSharedPreferencesWrite>()
    val saveSession: LiveData<SessionSharedPreferencesWrite>
        get() = _saveSession

    private fun checkField() {
        if (email.value.orEmpty().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()){
            fieldCorrect.value = true
        } else {
            inputLayoutEmail.value = R.string.invalid_email
            fieldCorrect.value = false
        }
        if(password.value.orEmpty() != passwordConfirmation.value.orEmpty()) {
            inputLayoutPasswordConfirmation.value = R.string.password_not_match
            fieldCorrect.value = false
        }
        if (password.value.orEmpty().contains(" ")) {
            inputLayoutPassword.value = R.string.password_have_space
            fieldCorrect.value = false
        }

        if (password.value.orEmpty().length < 8) {
            inputLayoutPassword.value = R.string.password_minimum_8
            fieldCorrect.value = false
        }

        if (password.value.orEmpty().isEmpty()) {
            inputLayoutPassword.value = R.string.empty_password
            fieldCorrect.value = false
        }
        if (fieldCorrect.value == false) {
            return
        }
        requestRegistration()
    }

    fun clickRegistration(emailView: String, passwordView: String, passwordConfirmationView: String) {
        email.value = emailView
        password.value = passwordView
        passwordConfirmation.value = passwordConfirmationView
        checkField()
    }

    private fun requestRegistration() {
        val user = UserSignUpRequest(
            email = email.value.orEmpty(),
            password =  password.value.orEmpty(),
            passwordConfirmation = passwordConfirmation.value.orEmpty(),
        )
        val request = SignUpRequest(session = SessionRequest(), user = user)
        _spinner.value = true

        BaseApi.retrofit(null).createUser(request).enqueue(object : Callback<AuthenticationResponse?> {
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
                _spinner.value = false
            }
            override fun onFailure(call: Call<AuthenticationResponse?>, t: Throwable) {
                _spinner.value = false
                _showToast.value = R.string.connection_issues
            }
        })
    }
}
