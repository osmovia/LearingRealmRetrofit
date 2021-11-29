package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.SessionManager
import com.example.learingrealmandretrofit.api.BaseApi
import com.example.learingrealmandretrofit.databinding.SettingsFragmentBinding
import com.example.learingrealmandretrofit.objects.response.Success
import com.example.learingrealmandretrofit.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment() {

    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSignOut.setOnClickListener {
            val token = SessionManager(requireContext()).fetchAuth().token!!
            BaseApi.retrofit.logOut(tokenSession = token, token = token).enqueue(object : Callback<Success?> {
                override fun onResponse(call: Call<Success?>, response: Response<Success?>) {
                    val statusCode = response.code()
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        when(body.success){
                            true -> logOut()
                            else -> Log.d("KEK", "Error")
                        }
                    }
                }
                override fun onFailure(call: Call<Success?>, t: Throwable) {
                    context?.showErrorToast(R.string.connection_issues)
                }
            })
        }
    }
    fun logOut() {
        SessionManager(requireContext()).clearSharedPreferences()
        val mainNavController: NavController?  = activity?.findNavController(R.id.containerView)
        mainNavController?.navigate(R.id.action_tabsFragment_to_authenticationFragment)
    }
}
