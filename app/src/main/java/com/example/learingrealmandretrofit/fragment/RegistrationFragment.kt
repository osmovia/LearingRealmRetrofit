package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.learingrealmandretrofit.databinding.RegistrationFragmnetBinding

class RegistrationFragment : Fragment() {
    private lateinit var binding: RegistrationFragmnetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegistrationFragmnetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.email.setOnFocusChangeListener { v, hasFocus ->

        }
    }
}