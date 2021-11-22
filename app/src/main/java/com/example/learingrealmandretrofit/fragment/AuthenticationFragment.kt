package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavHost
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.databinding.AuthenticationFragmentBinding

class AuthenticationFragment : Fragment() {

    private lateinit var binding: AuthenticationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AuthenticationFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}
