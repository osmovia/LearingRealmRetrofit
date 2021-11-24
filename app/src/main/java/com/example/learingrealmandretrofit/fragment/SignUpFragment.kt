package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.SignUpFragmentBinding
import com.example.learingrealmandretrofit.objects.response.Session
import com.example.learingrealmandretrofit.objects.response.Success
import com.example.learingrealmandretrofit.objects.response.User
import com.example.learingrealmandretrofit.objects.response.UserResponse
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
        binding.buttonRegister.setOnClickListener {
            val email = binding.email.text!!.toString()
            val password = binding.password.text!!.toString()
            val passwordConfirm = binding.confirmPassword.text!!.toString()

            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            } else {
                Toast.makeText(requireActivity(), "Invalid email, check the email entry.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (password.isEmpty()){
                Toast.makeText(requireActivity(), "You did not enter a password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (password.length < 8) {
                Toast.makeText(requireActivity(), "Password minimum length 8 symbol", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (password.contains(" ")) {
                Toast.makeText(requireActivity(), "Password must not have a space", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (password != passwordConfirm) {
                Toast.makeText(requireActivity(), "Password does not match please try again", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val user = User(
                email = email,
                password = password,
                password_confirmation = passwordConfirm
                )
            val session = Session("Android")
            val userJson = UserResponse(
                session = session,
                user = user
            )
            BaseApi.retrofit.createUser(userJson).enqueue(object : Callback<Success?> {
                override fun onResponse(call: Call<Success?>, response: Response<Success?>) {
                    Log.d("KEKA1", "${response.code()}")
                }
                override fun onFailure(call: Call<Success?>, t: Throwable) {
                    Log.d("KEKA1", "$t")
                }
            })
        }
    }
}
