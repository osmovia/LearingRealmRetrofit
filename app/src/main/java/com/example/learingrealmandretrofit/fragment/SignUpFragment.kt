package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.SignUpFragmentBinding
import com.example.learingrealmandretrofit.hideProgress
import com.example.learingrealmandretrofit.objects.response.Session
import com.example.learingrealmandretrofit.objects.response.Success
import com.example.learingrealmandretrofit.objects.response.User
import com.example.learingrealmandretrofit.objects.response.UserResponse
import com.example.learingrealmandretrofit.showProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {
    private lateinit var binding: SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignUpFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.textViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.email.setOnFocusChangeListener { _, _ ->
            binding.inputLayoutEmail.error = null
        }

        binding.password.setOnFocusChangeListener { _, _ ->
            binding.inputPassword.error = null
        }

        binding.confirmPassword.setOnFocusChangeListener { _, _ ->
            binding.inputConfirmPassword.error = null
        }

        binding.buttonRegister.setOnClickListener {
            var fieldCorrect: Boolean
            val email = binding.email.text!!.toString()
            val password = binding.password.text!!.toString()
            val passwordConfirm = binding.confirmPassword.text!!.toString()

            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                fieldCorrect = true
            } else {
                binding.inputLayoutEmail.error = "Invalid email, check the email entry"
                fieldCorrect = false
            }
            if (password != passwordConfirm) {
                binding.inputPassword.error = "Password does not match please try again"
                binding.inputConfirmPassword.error = "Password does not match please try again"
                fieldCorrect = false
            }
            if (password.contains(" ")) {
                binding.inputPassword.error = "Password must not have a space"
                fieldCorrect = false
            }
            if (password.length < 8) {
                binding.inputPassword.error = "Password minimum length 8 symbol"
                fieldCorrect = false
            }
            if (password.isEmpty()){
                binding.inputPassword.error = "You did not enter a password"
                fieldCorrect = false
            }
            if (!fieldCorrect) {
                return@setOnClickListener
            }

            val user = User(email = email, password = password, password_confirmation = passwordConfirm)
            val session = Session("Android")
            val userJson = UserResponse(session = session, user = user)
            activity?.showProgress()
            BaseApi.retrofit.createUser(userJson).enqueue(object : Callback<Success?> {
                override fun onResponse(call: Call<Success?>, response: Response<Success?>) {
                    activity?.hideProgress()
                    val mainNavController: NavController?  = activity?.findNavController(R.id.containerView)
                    mainNavController?.navigate(R.id.action_authenticationFragment_to_tabsFragment)
                }
                override fun onFailure(call: Call<Success?>, t: Throwable) {
                    activity?.hideProgress()
                }
            })
        }
    }
}
