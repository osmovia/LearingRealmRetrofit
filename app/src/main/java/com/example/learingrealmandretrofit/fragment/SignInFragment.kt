package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.SignInFragmentBinding
import com.example.learingrealmandretrofit.objects.request.SessionRequest
import com.example.learingrealmandretrofit.objects.request.SignInRequest
import com.example.learingrealmandretrofit.objects.request.UserSignInRequest
import com.example.learingrealmandretrofit.objects.response.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInFragment : Fragment() {

    private lateinit var binding: SignInFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignInFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mainNavController: NavController?  = activity?.findNavController(R.id.containerView)
        binding.buttonEntry.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val user = UserSignInRequest(email = email, password = password)
            val request = SignInRequest(session = SessionRequest(), user = user)
            activity?.showProgress()
            BaseApi.retrofit.signIn(request).enqueue(object : Callback<SignUpResponse?> {
                override fun onResponse(call: Call<SignUpResponse?>, response: Response<SignUpResponse?>
                ) {
                    val responseBody = response.body()
                    val statusCode = response.code()
                    if (response.isSuccessful && responseBody != null) {
                        SessionManager(requireContext()).saveAuth(
                            email = responseBody.user.email,
                            idUser = responseBody.session.userId,
                            token = responseBody.session.token
                        )
                        mainNavController?.navigate(R.id.action_authenticationFragment_to_tabsFragment)
                    } else {
                        activity?.hideProgress()
                        context?.showErrorCodeToast(statusCode)
                    }
                }
                override fun onFailure(call: Call<SignUpResponse?>, t: Throwable) {
                    activity?.hideProgress()
                    context?.showErrorToast(R.string.connection_issues)
                }
            })
        }
        binding.textViewSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
